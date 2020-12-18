package com.example.tasks.data.network.responses

import com.example.tasks.data.db.entities.Project

data class ProjectResponse(
    val success: Boolean,
    val data: Project
)