package com.radchuk.myapplication.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.Vehicle
import com.radchuk.myapplication.local.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MapFragment : Fragment() {
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView
    private var vehicles: List<Vehicle>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            googleMap = map
            fetchVehicles()
        }
        return view
    }

    private fun fetchVehicles() {
        ApiClient.apiService.getVehicles().enqueue(object : Callback<List<Vehicle>> {
            override fun onResponse(call: Call<List<Vehicle>>, response: Response<List<Vehicle>>) {
                if (response.isSuccessful) {
                    vehicles = response.body()
                    vehicles?.let { displayVehiclesOnMap(it) }
                } else {
                    // Обробити помилку
                }
            }

            override fun onFailure(call: Call<List<Vehicle>>, t: Throwable) {
                // Обробити помилку
            }
        })
    }

    private fun displayVehiclesOnMap(vehicles: List<Vehicle>) {

        val inflater = LayoutInflater.from(requireContext())

        for (vehicle in vehicles) {
            val location = vehicle.currentLocation.trim('(', ')').split(",")
            if (location.size == 2) {
                val latitude = location[0].trim().toDoubleOrNull()
                val longitude = location[1].trim().toDoubleOrNull()
                if (latitude != null && longitude != null) {
                    val latLng = LatLng(latitude, longitude)
                    val markerOptions = MarkerOptions().position(latLng).title(vehicle.model)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    markerOptions.snippet(vehicle.id.toString())
                    markerOptions.anchor(0.5f, 0.5f)
                    val marker = googleMap.addMarker(markerOptions)
                    marker?.tag = vehicle
                    googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                        override fun getInfoWindow(marker: Marker): View? {
                            return null
                        }
                        @SuppressLint("MissingInflatedId")
                        override fun getInfoContents(marker: Marker): View {
                            val view = inflater.inflate(R.layout.marker_info_window_layout, null)
                            val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
                            val detailsTextView = view.findViewById<TextView>(R.id.textViewDetails)
                            val vehicle = marker.tag as? Vehicle
                            vehicle?.let {
                                titleTextView.text = it.model
                                detailsTextView.text = """
                                Make: ${it.make}
                                Model: ${it.model}
                                Year: ${it.year}
                                Registration Number: ${it.registrationNumber}
                                Fuel Type: ${it.fuelType}
                                Tank Volume: ${it.tankVolume}
                                Engine Capacity: ${it.engineCapacity}
                                Current Status: ${it.currentStatus}
                                Category: ${it.categoryId?.name ?: "Unknown"}
                            """.trimIndent()
                            }
                            return view
                        }
                    })
                }
            }
        }

        val firstVehicle = vehicles.firstOrNull()
        firstVehicle?.let {
            val location = it.currentLocation.trim('(', ')').split(", ")
            if (location.size == 2) {
                val latitude = location[0].toDoubleOrNull()
                val longitude = location[1].toDoubleOrNull()
                if (latitude != null && longitude != null) {
                    val latLng = LatLng(latitude, longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}