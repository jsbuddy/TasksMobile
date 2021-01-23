package com.example.tasks.data.network

import com.example.tasks.data.network.payloads.LoginPayload
import com.example.tasks.data.network.responses.AuthResponse
import com.example.tasks.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AuthAPI {

    @POST("user/login")
    suspend fun login(@Body payload: LoginPayload): Response<AuthResponse>

    @GET("user")
    suspend fun getUser(@Header("Authorization") token: String): Response<AuthResponse>

    companion object {
        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        operator fun invoke(): AuthAPI {
            val okHttp = OkHttpClient.Builder()
                .addInterceptor(logger)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder().client(okHttp)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(AuthAPI::class.java)
        }
    }
}