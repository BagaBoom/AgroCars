package com.radchuk.myapplication.local

import okhttp3.OkHttpClient
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.1.1.94:8080/api/" //work
    //private const val BASE_URL = "http://172.20.10.11:8080/api/" //myInetIPhone

    private val okHttpClient: OkHttpClient = createOkHttpClient()

    private val retrofit = Retrofit.Builder()
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
                .header("KeyToken", "Bearer 78b06f58-92b2-45e0-8761-4f9cbfd5a9c5")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build()
    }
}

