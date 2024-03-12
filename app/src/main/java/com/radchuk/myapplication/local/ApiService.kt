package com.radchuk.myapplication.local

import com.radchuk.myapplication.data.CarCategory
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("car-categories")
    fun getCarCategories(): Call<List<CarCategory>>
}
