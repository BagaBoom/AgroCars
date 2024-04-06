package com.radchuk.myapplication.local

import com.radchuk.myapplication.data.CarCategory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CarCategotiesServise{
    @GET("car-categories")
    fun getCarCategories(): Call<List<CarCategory>>

    @GET("car-categories/name/{name}")
    fun getCarGategoryByName(@Path("name") name: String) : Call<CarCategory>

    @POST("car-categories")
    fun addCarCategories(@Body request: CarCategory) : Call<CarCategory>

    @PUT("car-categories/{id}")
    fun updateCategory(@Path("id") id : Long, @Body update : CarCategory): Call<CarCategory>

    @DELETE("car-categories/{id}")
    fun deleteCategory(@Path("id") id : Long): Call<CarCategory>
}