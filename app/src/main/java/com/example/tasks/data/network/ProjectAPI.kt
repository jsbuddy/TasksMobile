package com.example.tasks.data.network

import com.example.tasks.data.network.responses.ProjectsResponse
import com.example.tasks.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProjectAPI {
    @GET("projects")
    suspend fun getProjects(): Response<ProjectsResponse>

    companion object {
        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        operator fun invoke(): ProjectAPI {
            val okHttp = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder().client(okHttp)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ProjectAPI::class.java)
        }
    }
}