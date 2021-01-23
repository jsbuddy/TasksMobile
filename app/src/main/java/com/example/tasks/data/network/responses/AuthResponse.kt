package com.example.tasks.data.network.responses

import com.example.tasks.data.models.User

data class AuthResponse(
    val data: User,
    val success: Boolean,
    var token: String,
)
