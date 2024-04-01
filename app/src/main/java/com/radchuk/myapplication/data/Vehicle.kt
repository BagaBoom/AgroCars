package com.radchuk.myapplication.data

data class Vehicle(
    val id: Long?,
    val make: String,
    val model: String,
    val year: Int,
    val registrationNumber : String,
    val currentLocation : String,
    val fuelType : String,
    val tankVolume: Float,
    val engineCapacity: Float,
    val currentStatus: String,
    val categoryId: CarCategory?,
)