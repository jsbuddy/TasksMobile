package com.example.tasks.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ProjectApiInterceptor : Interceptor {
    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization", token!!)
            } ?: throw RuntimeException("Auth token should be defined for project apis")
        }.build()
        return chain.proceed(request)
    }
}