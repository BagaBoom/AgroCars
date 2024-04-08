package com.radchuk.myapplication.local


import com.radchuk.myapplication.data.CarService
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceCarService {

    @GET("services")
    fun getServices(): Call<List<CarService>>

    @POST("services")
    fun addService(@Body request: CarService) : Call<CarService>

    @PUT("services/{id}")
    fun updateService(@Path("id") id : Long, @Body update : CarService): Call<CarService>

    @DELETE("services/{id}")
    fun deleteService(@Path("id") id : Long): Call<CarService>
}