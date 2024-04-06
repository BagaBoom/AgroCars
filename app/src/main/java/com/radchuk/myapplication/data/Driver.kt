package com.radchuk.myapplication.data

data class Driver(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val licenseNumber: String,
    val statusDriver : String,
    val vehicle: Vehicle?
)
