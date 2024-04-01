package com.radchuk.myapplication.local

import android.content.Context
import android.util.Log
import com.radchuk.myapplication.App
import com.radchuk.myapplication.data.RefreshTokenRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object ApiClient {
    //const val BASE_URL = "http://10.1.1.94:8080/api/" //work
    //const val BASE_URL = "http://172.20.10.11:8080/api/" //myInetIPhone
    const val BASE_URL = "http://192.168.0.104:8080/api/" //myHome

    private var okHttpClient: OkHttpClient = createOkHttpClient()

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    private fun createOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val accessToken = getAccessToken()
            val refreshToken = getRefreshToken()
            if (accessToken.isNotEmpty()) {
                //Log.d("MyLog", "Main = " + accessToken)
                requestBuilder.header("Authorization", "Bearer $accessToken")
            }
            val request = requestBuilder.build()
            var response = chain.proceed(request)

            if (response.code() == 500) {
                response.close()
                val refreshedTokenAAA = refreshAccessToken(refreshToken)
                saveAccessToken(refreshedTokenAAA)
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer $refreshedTokenAAA")
                    .build()
                response = chain.proceed(newRequest)
                //Log.d("MyLog", "NewToken $refreshedTokenAAA")
            }
            response
        }
        return httpClient.build()
    }

    private fun refreshAccessToken(refreshToken: String): String {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authService = retrofit.create(AuthService::class.java)
        val request = RefreshTokenRequest(refreshToken)
        val call = authService.refreshAccessToken(request)
        val response = call.execute()
        return if (response.isSuccessful) {
            response.body()?.token ?: "null"
        } else {
            ""
        }
    }

    private fun saveAccessToken(newAccessToken: String) {
        val sharedPreferences = App.instance.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", newAccessToken)
        editor.apply()
    }

    private fun getAccessToken(): String {
        val sharedPreferences = App.instance.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPreferences.getString("accessToken", "") ?: ""
    }

    private fun getRefreshToken(): String {
        val sharedPreferences = App.instance.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPreferences.getString("refreshToken", "") ?: ""
    }
}

