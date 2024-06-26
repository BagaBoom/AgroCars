package com.radchuk.myapplication.data

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)