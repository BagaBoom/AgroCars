package com.radchuk.myapplication.local

import com.radchuk.myapplication.data.Driver

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DriverServise {
    @GET("drivers")
    fun getDrivers(): Call<List<Driver>>

    @POST("drivers")
    fun addDriver(@Body request: Driver) : Call<Driver>

    @PUT("drivers/{id}")
    fun updateDriver(@Path("id") id : Long, @Body update : Driver): Call<Driver>

    @DELETE("drivers/{id}")
    fun deleteDriver(@Path("id") id : Long): Call<Driver>
}