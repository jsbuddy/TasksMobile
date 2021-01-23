package com.example.tasks.ui.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.network.payloads.CreateProjectPayload
import com.example.tasks.data.repositories.AuthRepository
import com.example.tasks.data.repositories.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ProjectsViewModel @ViewModelInject constructor(
    private val projectRepository: ProjectRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val projects: Flow<List<Project>> = projectRepository.getProjects().map {
        it.sortedWith { t, t2 -> comparator(t, t2) }
    }

    private val _newProjectUiState = MutableStateFlow<UiState>(UiState.Empty)
    val newProjectUiState = _newProjectUiState.asStateFlow()

    sealed class UiState {
        object Loading : UiState()
        class Error(val message: String) : UiState()
        object Empty : UiState()
        object Success : UiState()
    }

    init {
        fetchProjects()
    }

    fun fetchProjects() = viewModelScope.launch {
        val response = projectRepository.fetchProjects()
        if (response.isSuccessful) {
            response.body()?.let {
                projectRepository.deleteProjects()
                projectRepository.insertProjects(it.data)
            }
        }
    }

    private fun comparator(p1: Project, p2: Project): Int {
        val date1 = LocalDateTime.parse(p1.createdAt.replace("Z", ""))
        val date2 = LocalDateTime.parse(p2.createdAt.replace("Z", ""))
        return if (date1.isAfter(date2)) -1 else 1
    }

    fun createProject(name: String, deadline: String?) = viewModelScope.launch {
        _newProjectUiState.value = UiState.Loading
        val response = projectRepository.createProject(CreateProjectPayload(name, deadline))
        if (response.isSuccessful) {
            response.body()?.let {
                projectRepository.insertProject(it.data)
                _newProjectUiState.value = UiState.Success
            }
        } else {
            _newProjectUiState.value = UiState.Error("Unable to create project, please try again")
        }
    }

    fun resetProjectUiState() {
        _newProjectUiState.value = UiState.Empty
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
        projectRepository.deleteProjects()
        projectRepository.deleteTasks()
    }
}