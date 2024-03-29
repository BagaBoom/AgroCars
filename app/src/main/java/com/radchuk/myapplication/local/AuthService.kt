package com.radchuk.myapplication.local

import com.radchuk.myapplication.data.AuthRequest
import com.radchuk.myapplication.data.AuthResponse
import com.radchuk.myapplication.data.RefreshTokenRequest
import com.radchuk.myapplication.data.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @POST("auth/refresh")
    fun refreshAccessToken(@Body request: RefreshTokenRequest): Call<RefreshTokenResponse>
}

