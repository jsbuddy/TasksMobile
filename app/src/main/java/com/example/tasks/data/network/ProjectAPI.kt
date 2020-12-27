package com.example.tasks.data.network

import com.example.tasks.data.network.payloads.CreateProjectPayload
import com.example.tasks.data.network.payloads.CreateTaskPayload
import com.example.tasks.data.network.payloads.UpdateTaskPayload
import com.example.tasks.data.network.responses.ProjectResponse
import com.example.tasks.data.network.responses.ProjectsResponse
import com.example.tasks.data.network.responses.TaskResponse
import com.example.tasks.data.network.responses.TasksResponse
import com.example.tasks.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ProjectAPI {
    @GET("projects")
    suspend fun getProjects(): Response<ProjectsResponse>

    @GET("projects/{id}/tasks")
    suspend fun getTasks(@Path("id") id: String): Response<TasksResponse>

    @POST("projects")
    suspend fun createProject(@Body payload: CreateProjectPayload): Response<ProjectResponse>

    @GET("projects/{id}")
    suspend fun getProject(@Path("id") id: String): Response<ProjectResponse>

    @DELETE("projects/{id}")
    suspend fun deleteProject(@Path("id") id: String): Response<ProjectResponse>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<TaskResponse>

    @POST("tasks")
    suspend fun createTask(@Body payload: CreateTaskPayload): Response<TaskResponse>

    @PATCH("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Body payload: UpdateTaskPayload,
    ): Response<TaskResponse>

    companion object {
        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        operator fun invoke(): ProjectAPI {
            val okHttp = OkHttpClient.Builder()
                .addInterceptor(logger)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder().client(okHttp)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ProjectAPI::class.java)
        }
    }
}