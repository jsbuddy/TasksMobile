package com.example.tasks.ui.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.tasks.data.ProjectRepository
import com.example.tasks.data.db.entities.Project
import kotlinx.coroutines.flow.Flow

class ProjectsViewModel @ViewModelInject constructor(
    repository: ProjectRepository
) : ViewModel() {

    val projects: Flow<List<Project>> = repository.getProjects()
}