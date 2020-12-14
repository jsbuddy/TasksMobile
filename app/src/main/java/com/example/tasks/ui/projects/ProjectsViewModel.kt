package com.example.tasks.ui.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.ProjectRepository
import com.example.tasks.data.db.entities.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class ProjectsViewModel @ViewModelInject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    val projects: Flow<List<Project>> = repository.loadProjects()

    init {
        fetchProjects()
    }

    private fun fetchProjects() = viewModelScope.launch {
        val response = repository.fetchProjects()
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertProjects(it.data)
            }
        }
    }
}