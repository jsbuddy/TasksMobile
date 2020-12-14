package com.example.tasks.data.network.responses

import com.example.tasks.data.db.entities.Project

data class ProjectsResponse(
    val success: Boolean,
    val data: List<Project>
)