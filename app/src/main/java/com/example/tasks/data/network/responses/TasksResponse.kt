package com.example.tasks.data.network.responses

import com.example.tasks.data.db.entities.Task

data class TasksResponse(
    val success: Boolean,
    val data: List<Task>
)