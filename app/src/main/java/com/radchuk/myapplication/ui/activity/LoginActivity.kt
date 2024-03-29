package com.radchuk.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.radchuk.myapplication.data.AuthRequest
import com.radchuk.myapplication.data.AuthResponse
import com.radchuk.myapplication.databinding.ActivityLoginBinding
import com.radchuk.myapplication.local.ApiClient
import com.radchuk.myapplication.local.ApiService
import com.radchuk.myapplication.local.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val email = binding.editTextTextLogin.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            authenticateUser(email, password)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = authenticate(email, password)
                val accessToken = response.accessToken
                val refreshToken = response.refreshToken

                Log.d("MyLog", "a = $accessToken , r = $refreshToken")
                // Зберегти токени в SharedPreferences
                saveTokens(accessToken, refreshToken)
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            } catch (e: Exception) {
                // Обробка помилок
                e.printStackTrace()
            }
        }
    }

    private suspend fun authenticate(email: String, password: String): AuthResponse {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(ApiService::class.java)
        return authService.authenticate(AuthRequest(email, password))
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.apply()
    }
}
