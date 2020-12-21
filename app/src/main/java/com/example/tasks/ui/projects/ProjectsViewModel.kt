package com.example.tasks.ui.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.repositories.ProjectRepository
import com.example.tasks.data.db.entities.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ProjectsViewModel @ViewModelInject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    val projects: Flow<List<Project>> = repository.getProjects().map {
        it.sortedWith { t, t2 -> comparator(t, t2) }
    }

    init {
        fetchProjects()
    }

    private fun fetchProjects() = viewModelScope.launch {
        val response = repository.fetchProjects()
        if (response.isSuccessful) {
            response.body()?.let {
                repository.deleteProjects()
                repository.insertProjects(it.data)
            }
        }
    }

    private fun comparator(p1: Project, p2: Project): Int {
        val date1 = LocalDateTime.parse(p1.createdAt.replace("Z", ""))
        val date2 = LocalDateTime.parse(p2.createdAt.replace("Z", ""))
        return if (date1.isAfter(date2)) -1 else 1
    }
}