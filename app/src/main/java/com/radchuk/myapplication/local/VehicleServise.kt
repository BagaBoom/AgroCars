package com.radchuk.myapplication.local


import com.radchuk.myapplication.data.CarCategory
import com.radchuk.myapplication.data.Vehicle
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VehicleServise {

    @GET("vehicle")
    fun getVehicles(): Call<List<Vehicle>>


    @GET("vehicle/reg/{number}")
    fun getVehicleByRegistrationNumber(@Path("number") number: String) : Call<Vehicle>

    @POST("vehicle")
    fun addVehicle(@Body request: Vehicle) : Call<Vehicle>

    @PUT("vehicle/{id}")
    fun updateVehicle(@Path("id") id : Long, @Body update : Vehicle): Call<Vehicle>

    @DELETE("vehicle/{id}")
    fun deleteVehicle(@Path("id") id : Long): Call<Vehicle>
}