package com.radchuk.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.CarCategory
import com.radchuk.myapplication.data.Vehicle
import com.radchuk.myapplication.databinding.FragmentVehicleBinding
import com.radchuk.myapplication.local.ApiClient
import com.radchuk.myapplication.ui.activity.LoginActivity
import com.radchuk.myapplication.ui.adapters.CarCategoryAdapter
import com.radchuk.myapplication.ui.adapters.VehicleAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VehicleFragment : Fragment(), VehicleAdapter.OnVehicleInteractionListener {

    private lateinit var binding: FragmentVehicleBinding
    private lateinit var adapter: VehicleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVehicleBinding.inflate(inflater, container , false)


        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = VehicleAdapter(this)
        binding.recyclerView.adapter = adapter
        updateListVehicle()
        binding.buttonAddVehicle.setOnClickListener {
            showAddVehicle()
        }

        return binding.root
    }

    private fun updateListVehicle(){
        ApiClient.apiService.getVehicles().enqueue(object : Callback<List<Vehicle>> {
            override fun onResponse(call: Call<List<Vehicle>>, response: Response<List<Vehicle>>) {
                if (response.isSuccessful) {
                    val vehicles = response.body()
                    adapter.updateData(vehicles ?: emptyList())
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }

            override fun onFailure(call: Call<List<Vehicle>>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }
    private fun showAddVehicle(){
        val dialogView = layoutInflater.inflate(R.layout.item_vehicle_dialog, null)
        val editTextMake : EditText = dialogView.findViewById(R.id.editMake)
        val editTextModel : EditText = dialogView.findViewById(R.id.editModel)
        val editTextYear : EditText = dialogView.findViewById(R.id.editYear)
        val editTextRegistrationNumber: EditText = dialogView.findViewById(R.id.editRegistrationNumber)
        val editTextCurrentLocation: EditText = dialogView.findViewById(R.id.editCurrentLocation)
        val editTextFuelType: EditText = dialogView.findViewById(R.id.editFuelType)
        val editTextTankVolume: EditText = dialogView.findViewById(R.id.editTankVolume)
        val editTextEngineCapacity: EditText = dialogView.findViewById(R.id.editEngineCapacity)
        val editTextCurrentStatus: EditText = dialogView.findViewById(R.id.editCurrentStatus)
        val editTextCategoryId: EditText = dialogView.findViewById(R.id.editCategoryId)


        AlertDialog.Builder(requireContext())
            .setTitle("Додати Транспорт")
            .setView(dialogView)


            .setPositiveButton("Save") { dialog, _ ->
                val make = editTextMake.text.toString()
                val model = editTextModel.text.toString()
                val year = editTextYear.text.toString()
                val registrationNumber = editTextRegistrationNumber.text.toString()
                val currentLocation = editTextCurrentLocation.text.toString()
                val fuelType = editTextFuelType.text.toString()
                val tankVolume = editTextTankVolume.text.toString()
                val engineCapacity = editTextEngineCapacity.text.toString()
                val currentStatus = editTextCurrentStatus.text.toString()
                val categoryName = editTextCategoryId.text.toString()
                // Перевірка на пусте значення
                if (make.isBlank() || model.isBlank() || year.isBlank() || registrationNumber.isBlank() ||
                    currentLocation.isBlank() || fuelType.isBlank() || tankVolume.isBlank() ||
                    engineCapacity.isBlank() || currentStatus.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Перевірка на числове значення року
                val yearInt = try {
                    year.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid year format", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Перевірка на числове значення об'єму бака
                val tankVolumeFloat = try {
                    tankVolume.toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid tank volume format", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Перевірка на числове значення об'єму двигуна
                val engineCapacityFloat = try {
                    engineCapacity.toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid engine capacity format", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                var newCarCategory : CarCategory?
                if(categoryName.isNotBlank()){
                    ApiClient.apiService.getCarGategoryByName(categoryName).enqueue(object : Callback<CarCategory> {
                        override fun onResponse(call: Call<CarCategory>, response: Response<CarCategory>) {
                            if (response.isSuccessful) {
                                newCarCategory = response.body()
                                val addVehicle = Vehicle(null,make, model, yearInt, registrationNumber, currentLocation, fuelType, tankVolumeFloat,
                                    engineCapacityFloat, currentStatus, newCarCategory)
                                addVehicle(addVehicle)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<CarCategory>, t: Throwable) {
                            Log.i("MyLog", "Network request failed: ${t.message}")
                        }
                    })
                }else{
                    Toast.makeText(requireContext(), "Не получилося додати", Toast.LENGTH_SHORT).show()
                }



            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditVehicle(vehicle: Vehicle){
        val dialogView = layoutInflater.inflate(R.layout.item_vehicle_dialog, null)

       val editTextMake : EditText = dialogView.findViewById(R.id.editMake)
       val editTextModel : EditText = dialogView.findViewById(R.id.editModel)
       val editTextYear : EditText = dialogView.findViewById(R.id.editYear)
       val editTextRegistrationNumber: EditText = dialogView.findViewById(R.id.editRegistrationNumber)
       val editTextCurrentLocation: EditText = dialogView.findViewById(R.id.editCurrentLocation)
       val editTextFuelType: EditText = dialogView.findViewById(R.id.editFuelType)
       val editTextTankVolume: EditText = dialogView.findViewById(R.id.editTankVolume)
       val editTextEngineCapacity: EditText = dialogView.findViewById(R.id.editEngineCapacity)
       val editTextCurrentStatus: EditText = dialogView.findViewById(R.id.editCurrentStatus)
       val editTextCategoryId: EditText = dialogView.findViewById(R.id.editCategoryId)

        editTextMake.setText(vehicle.make)
        editTextModel.setText(vehicle.model)
        editTextYear.setText(vehicle.year.toString())
        editTextRegistrationNumber.setText(vehicle.registrationNumber)
        editTextCurrentLocation.setText(vehicle.currentLocation)
        editTextFuelType.setText(vehicle.fuelType)
        editTextTankVolume.setText(vehicle.tankVolume.toString())
        editTextEngineCapacity.setText(vehicle.engineCapacity.toString())
        editTextCurrentStatus.setText(vehicle.currentStatus)
        editTextCategoryId.setText(vehicle.categoryId?.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Редагувати Транспорт")
            .setView(dialogView)


            .setPositiveButton("Save") { dialog, _ ->
                val make = editTextMake.text.toString()
                val model = editTextModel.text.toString()
                val year = editTextYear.text.toString()
                val registrationNumber = editTextRegistrationNumber.text.toString()
                val currentLocation = editTextCurrentLocation.text.toString()
                val fuelType = editTextFuelType.text.toString()
                val tankVolume = editTextTankVolume.text.toString()
                val engineCapacity = editTextEngineCapacity.text.toString()
                val currentStatus = editTextCurrentStatus.text.toString()
                val categoryName = editTextCategoryId.text.toString()
                // Перевірка на пусте значення
                if (make.isBlank() || model.isBlank() || year.isBlank() || registrationNumber.isBlank() ||
                    currentLocation.isBlank() || fuelType.isBlank() || tankVolume.isBlank() ||
                    engineCapacity.isBlank() || currentStatus.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Перевірка на числове значення року
                val yearInt = try {
                    year.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid year format", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Перевірка на числове значення об'єму бака
                val tankVolumeFloat = try {
                    tankVolume.toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid tank volume format", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Перевірка на числове значення об'єму двигуна
                val engineCapacityFloat = try {
                    engineCapacity.toFloat()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Invalid engine capacity format", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                var newCarCategory : CarCategory? = vehicle.categoryId
                if(vehicle.categoryId?.name != categoryName){
                    ApiClient.apiService.getCarGategoryByName(categoryName).enqueue(object : Callback<CarCategory> {
                        override fun onResponse(call: Call<CarCategory>, response: Response<CarCategory>) {
                            if (response.isSuccessful) {
                                newCarCategory = response.body()
                                val updateVehicle = Vehicle(vehicle.id,make, model, yearInt, registrationNumber, currentLocation, fuelType, tankVolumeFloat,
                                    engineCapacityFloat, currentStatus, newCarCategory)
                                editVehicle(updateVehicle)
                                dialog.dismiss()
                            } else {
                                Log.i("MyLog", "Failed to edit category: ${response.errorBody()}")
                            }
                        }

                        override fun onFailure(call: Call<CarCategory>, t: Throwable) {
                            newCarCategory = vehicle.categoryId
                            Log.i("MyLog", "Network request failed: ${t.message}")
                        }
                    })
                }else{
                    val updateVehicle = Vehicle(vehicle.id,make, model, yearInt, registrationNumber, currentLocation, fuelType, tankVolumeFloat,
                        engineCapacityFloat, currentStatus, newCarCategory)
                    editVehicle(updateVehicle)
                    dialog.dismiss()
                }



            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private  fun editVehicle(newvehicle: Vehicle){
        ApiClient.apiService.updateVehicle(newvehicle.id!!, newvehicle).enqueue(object : Callback<Vehicle> {
            override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                if (response.isSuccessful) {
                    updateListVehicle()
                }
            }

            override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }
    private fun addVehicle(vehicle: Vehicle){
        ApiClient.apiService.addVehicle(vehicle).enqueue(object : Callback<Vehicle> {
            override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                if (response.isSuccessful) {
                    updateListVehicle()
                }
            }

            override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }

    private fun showDeleteDialog(vehicle: Vehicle){
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete ${vehicle.model}?")
            .setPositiveButton("Yes") { _, _ ->
                deleteVehicle(vehicle)
            }
            .setNegativeButton("No", null)
            .create()
            .show()


    }
    private fun deleteVehicle(vehicle: Vehicle){
        ApiClient.apiService.deleteVehicle(vehicle.id!!).enqueue(object : Callback<Vehicle> {
            override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                if (response.isSuccessful) {
                    updateListVehicle()
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }

            override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")

            }
        })
    }

    override fun onEditClicked(vehicle: Vehicle) {
        showEditVehicle(vehicle)
    }

    override fun onDeleteClicked(vehicle: Vehicle) {
        showDeleteDialog(vehicle)
    }


}