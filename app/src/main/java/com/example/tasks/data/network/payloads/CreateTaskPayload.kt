package com.example.tasks.data.network.payloads

data class CreateTaskPayload(
    val name: String,
    val priority: Int,
    val due: String,
    val project: String
)