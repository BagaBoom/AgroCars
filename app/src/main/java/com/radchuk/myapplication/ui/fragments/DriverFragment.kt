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
import androidx.recyclerview.widget.LinearLayoutManager
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.Driver
import com.radchuk.myapplication.data.Vehicle
import com.radchuk.myapplication.databinding.FragmentDriverBinding
import com.radchuk.myapplication.local.ApiClient
import com.radchuk.myapplication.ui.activity.LoginActivity
import com.radchuk.myapplication.ui.adapters.DriverAdapter
import com.radchuk.myapplication.ui.adapters.VehicleAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AlertDialog
import com.radchuk.myapplication.data.CarCategory

class DriverFragment : Fragment(),DriverAdapter.OnDriverInteractionListener {

    private lateinit var binding : FragmentDriverBinding
    private lateinit var adapter: DriverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = DriverAdapter(this)
        binding.recyclerView.adapter = adapter
        updateListDriver()
        binding.buttonAddDriver.setOnClickListener {
            showAddDriver()
        }


        return binding.root
    }

    private fun updateListDriver(){
        ApiClient.apiService.getDrivers().enqueue(object : Callback<List<Driver>> {
            override fun onResponse(call: Call<List<Driver>>, response: Response<List<Driver>>) {
                if (response.isSuccessful) {
                    val drivers = response.body()
                    adapter.updateData(drivers ?: emptyList())
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }

            override fun onFailure(call: Call<List<Driver>>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }

    private fun showAddDriver(){
        val dialogView = layoutInflater.inflate(R.layout.item_driver_dialog, null)
        val editTextFirtsName : EditText = dialogView.findViewById(R.id.editFirtsName)
        val editTextLastName : EditText = dialogView.findViewById(R.id.editLastName)
        val editTextLicenseNumber : EditText = dialogView.findViewById(R.id.editLicenseNumber)
        val editTextStatusDriver : EditText = dialogView.findViewById(R.id.editStatusDriver)
        val editTextVehicle : EditText = dialogView.findViewById(R.id.editVehicle)


        AlertDialog.Builder(requireContext())
            .setTitle("Додати Водія")
            .setView(dialogView)


            .setPositiveButton("Save") { dialog, _ ->
                val firstName = editTextFirtsName.text
                val lastName = editTextLastName.text
                val licenseNumber = editTextLicenseNumber.text
                val statusDriver = editTextStatusDriver.text
                val vehicle = editTextVehicle.text

                if (firstName.isBlank() || lastName.isBlank() || licenseNumber.isBlank() || statusDriver.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }


                var newVehicle : Vehicle?
                if(vehicle.isNotBlank()){
                    ApiClient.apiService.getVehicleByRegistrationNumber(vehicle.toString()).enqueue(object : Callback<Vehicle> {
                        override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                            if (response.isSuccessful) {
                                newVehicle = response.body()
                                val addDriver = Driver(
                                    null,
                                    firstName.toString(),
                                    lastName.toString(),
                                    licenseNumber.toString(),
                                    statusDriver.toString(),
                                    newVehicle
                                )
                                addDriver(addDriver)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                        }
                    })
                }else{
                    val addDriver = Driver(
                        null,
                        firstName.toString(),
                        lastName.toString(),
                        licenseNumber.toString(),
                        statusDriver.toString(),
                        null
                    )
                    addDriver(addDriver)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



    private fun showEditDriver(driver: Driver){
        val dialogView = layoutInflater.inflate(R.layout.item_driver_dialog, null)
        val editTextFirtsName : EditText = dialogView.findViewById(R.id.editFirtsName)
        val editTextLastName : EditText = dialogView.findViewById(R.id.editLastName)
        val editTextLicenseNumber : EditText = dialogView.findViewById(R.id.editLicenseNumber)
        val editTextStatusDriver : EditText = dialogView.findViewById(R.id.editStatusDriver)
        val editTextVehicle : EditText = dialogView.findViewById(R.id.editVehicle)


        editTextFirtsName.setText(driver.firstName)
        editTextLastName.setText(driver.lastName)
        editTextLicenseNumber.setText(driver.licenseNumber)
        editTextStatusDriver.setText(driver.statusDriver)
        editTextVehicle.setText(driver.vehicle?.registrationNumber)


        AlertDialog.Builder(requireContext())
            .setTitle("Редагувати Водія")
            .setView(dialogView)


            .setPositiveButton("Save") { dialog, _ ->
                val firstName = editTextFirtsName.text.toString()
                val lastName = editTextLastName.text.toString()
                val licenseNumber = editTextLicenseNumber.text.toString()
                val statusDriver = editTextStatusDriver.text.toString()
                val vehicle = editTextVehicle.text.toString()

                if (firstName.isBlank() || lastName.isBlank() || licenseNumber.isBlank() || statusDriver.isBlank()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }


                var newVehicle : Vehicle?
                if(driver.vehicle?.registrationNumber != vehicle){
                    ApiClient.apiService.getVehicleByRegistrationNumber(vehicle).enqueue(object : Callback<Vehicle> {
                        override fun onResponse(call: Call<Vehicle>, response: Response<Vehicle>) {
                            if (response.isSuccessful) {
                                newVehicle = response.body()
                                val updateDriver = Driver(
                                    driver.id,
                                    firstName,
                                    lastName,
                                    licenseNumber,
                                    statusDriver,
                                    newVehicle
                                )
                                editDriver(updateDriver)
                                dialog.dismiss()
                            } else {
                                newVehicle = response.body()
                                val updateDriver = Driver(
                                    driver.id,
                                    firstName,
                                    lastName,
                                    licenseNumber,
                                    statusDriver,
                                    null
                                )
                                editDriver(updateDriver)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Vehicle>, t: Throwable) {
                            newVehicle = null
                        }
                    })
                }else{
                    val updateDriver = Driver(
                        driver.id,
                        firstName,
                        lastName,
                        licenseNumber,
                        statusDriver,
                        driver.vehicle
                    )
                    editDriver(updateDriver)
                    dialog.dismiss()
                }



            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showDeleteDialog(driver: Driver){
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete ${driver.firstName} ${driver.lastName}?")
            .setPositiveButton("Yes") { _, _ ->
                deleteDriver(driver)
            }
            .setNegativeButton("No", null)
            .create()
            .show()


    }
    private fun deleteDriver(driver: Driver){
        ApiClient.apiService.deleteDriver(driver.id!!).enqueue(object : Callback<Driver> {
            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if (response.isSuccessful) {
                    updateListDriver()
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                    Log.i("MyLog", "Failed to fetch car categories: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Driver>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")

            }
        })

    }
    private fun addDriver(driver: Driver){
        ApiClient.apiService.addDriver(driver).enqueue(object : Callback<Driver> {
            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if (response.isSuccessful) {
                    updateListDriver()
                }
            }

            override fun onFailure(call: Call<Driver>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }

    private fun editDriver(newdriver: Driver){
        ApiClient.apiService.updateDriver(newdriver.id!!, newdriver).enqueue(object : Callback<Driver> {
            override fun onResponse(call: Call<Driver>, response: Response<Driver>) {
                if (response.isSuccessful) {
                    updateListDriver()
                }
            }

            override fun onFailure(call: Call<Driver>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }

    override fun onEditClicked(driver: Driver) {
        showEditDriver(driver)
    }

    override fun onDeleteClicked(driver: Driver) {
        showDeleteDialog(driver)
    }


}