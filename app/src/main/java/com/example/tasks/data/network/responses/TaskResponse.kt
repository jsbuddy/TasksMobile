package com.example.tasks.data.network.responses

import com.example.tasks.data.db.entities.Task

data class TaskResponse(
    val success: Boolean,
    val data: Task
)