package com.radchuk.myapplication.local

import com.radchuk.myapplication.data.CarService
import com.radchuk.myapplication.data.InfoCountsObjest
import retrofit2.Call
import retrofit2.http.GET


interface ApiService : CarCategotiesServise, AuthService, VehicleServise, DriverServise ,ServiceCarService{

    @GET("count")
    fun getCountObject(): Call<InfoCountsObjest>
}



