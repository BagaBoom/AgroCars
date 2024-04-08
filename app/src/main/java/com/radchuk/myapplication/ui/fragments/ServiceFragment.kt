package com.radchuk.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.CarService
import com.radchuk.myapplication.data.Vehicle
import com.radchuk.myapplication.databinding.FragmentServiseBinding
import com.radchuk.myapplication.local.ApiClient
import com.radchuk.myapplication.ui.activity.LoginActivity
import com.radchuk.myapplication.ui.adapters.CarServiceAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class ServiceFragment : Fragment(), CarServiceAdapter.OnCarServiceInteractionListener {

    private lateinit var binding: FragmentServiseBinding
    private lateinit var adapter: CarServiceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiseBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = CarServiceAdapter(this)
        binding.recyclerView.adapter = adapter
        updateListCarService()
        binding.buttonAddService.setOnClickListener {
            showAddCarService()
        }
        return binding.root
    }

    private fun updateListCarService(){
        ApiClient.apiService.getServices().enqueue(object : Callback<List<CarService>> {
            override fun onResponse(call: Call<List<CarService>>, response: Response<List<CarService>>) {
                if (response.isSuccessful) {
                    val carServices = response.body()
                    adapter.updateData(carServices ?: emptyList())
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }

            override fun onFailure(call: Call<List<CarService>>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }
    fun addCarService(carService: CarService){
        ApiClient.apiService.addService(carService).enqueue(object : Callback<CarService> {
            override fun onResponse(call: Call<CarService>, response: Response<CarService>) {
                if (response.isSuccessful) {
                    updateListCarService()
                }else{
                    Log.i("MyLog", "Nen")
                }
            }

            override fun onFailure(call: Call<CarService>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }
    private fun editCarService(carService: CarService){
        ApiClient.apiService.updateService(carService.id!!, carService).enqueue(object : Callback<CarService> {
            override fun onResponse(call: Call<CarService>, response: Response<CarService>) {
                if (response.isSuccessful) {
                    updateListCarService()
                }
            }

            override fun onFailure(call: Call<CarService>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }
    private fun deleteCarService(carService: CarService){
        ApiClient.apiService.deleteService(carService.id!!).enqueue(object : Callback<CarService> {
            override fun onResponse(call: Call<CarService>, response: Response<CarService>) {
                if (response.isSuccessful) {
                    updateListCarService()
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                    Log.i("MyLog", "Failed to fetch car categories: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<CarService>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")

            }
        })
    }

    private fun showEditCarService(carService: CarService){
        val dialogView = layoutInflater.inflate(R.layout.item_car_service_dialog, null)
        val editTextServiceDate : EditText = dialogView.findViewById(R.id.editServiceDate)
        val editTextServiceType : EditText = dialogView.findViewById(R.id.editServiceType)
        val editTextRegistrationNumber : EditText = dialogView.findViewById(R.id.editServiceVehicleByRegistrationNumber)


        editTextServiceDate.setText(convertDateTimeFormattoUI(carService.serviceDate.toString()))
        editTextServiceType.setText(carService.serviceType)
        editTextRegistrationNumber.setText(carService.vehicle?.registrationNumber)



        AlertDialog.Builder(requireContext())
            .setTitle("Редагувати Сервіс")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val serviceDate = editTextServiceDate.text.toString()
                val serviceType = editTextServiceType.text
                val registrationNumber = editTextRegistrationNumber.text


                if (serviceDate.isBlank() || serviceType.isBlank() || registrationNumber.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                var newVehicle : Vehicle?
                if(carService.vehicle?.registrationNumber != registrationNumber.toString()){
                    ApiClient.apiService.getVehicleByRegistrationNumber(registrationNumber.toString()).enqueue(object : Callback<Vehicle> {
                        override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                            if (response.isSuccessful) {
                                newVehicle = response.body()
                                Log.d("MyLog", convertDateFormatToRequest(serviceDate).toString())
                                val updateCarService = CarService(
                                    carService.id,
                                    convertDateFormatToRequest(serviceDate),
                                    serviceType.toString(),
                                    newVehicle
                                )
                                editCarService(updateCarService)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                            newVehicle = null
                        }
                    })
                }else{
                    val updateCarService = CarService(
                        carService.id,
                        convertDateFormatToRequest(serviceDate),
                        serviceType.toString(),
                        carService.vehicle
                    )
                    editCarService(updateCarService)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAddCarService(){
        val dialogView = layoutInflater.inflate(R.layout.item_car_service_dialog, null)

        val editTextServiceDate : EditText = dialogView.findViewById(R.id.editServiceDate)
        val editTextServiceType : EditText = dialogView.findViewById(R.id.editServiceType)
        val editTextRegistrationNumber : EditText = dialogView.findViewById(R.id.editServiceVehicleByRegistrationNumber)


        AlertDialog.Builder(requireContext())
            .setTitle("Додати Сервіс")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val serviceDate = editTextServiceDate.text.toString()
                val serviceType = editTextServiceType.text
                val registrationNumber = editTextRegistrationNumber.text


                if (serviceDate.isBlank() || serviceType.isBlank() || registrationNumber.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                var newVehicle : Vehicle?
                if(registrationNumber.isNotBlank()){
                    ApiClient.apiService.getVehicleByRegistrationNumber(registrationNumber.toString()).enqueue(object : Callback<Vehicle> {
                        override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                            if (response.isSuccessful) {
                                newVehicle = response.body()
                                Log.d("MyLog", convertDateFormatToRequest(serviceDate).toString())

                                val addCarService = CarService(
                                    null,
                                    convertDateFormatToRequest(serviceDate),
                                    serviceType.toString(),
                                    newVehicle
                                )
                                addCarService(addCarService)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                        }
                    })
                }else{
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun showDeleteCarService(carService: CarService){
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete  ${carService.serviceType}?")
            .setPositiveButton("Yes") { _, _ ->
                deleteCarService(carService)
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    override fun onEditClicked(carService: CarService) {
        showEditCarService(carService)
    }

    override fun onDeleteClicked(carService: CarService) {
        showDeleteCarService(carService)
    }

    fun convertDateFormatToRequest(inputDate: String): String? {
        try {
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun convertDateTimeFormattoUI(inputDateTime: String): String? {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDateTime)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}