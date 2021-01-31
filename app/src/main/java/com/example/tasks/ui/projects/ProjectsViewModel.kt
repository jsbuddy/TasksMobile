package com.example.tasks.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.network.payloads.CreateProjectPayload
import com.example.tasks.data.repositories.AuthRepository
import com.example.tasks.data.repositories.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val projects: Flow<List<Project>> = projectRepository.getProjects().map {
        it.sortedWith { t, t2 -> comparator(t, t2) }
    }

    private val _projectsUiState = MutableStateFlow<UiState>(UiState.Empty)
    val projectsUiState = _projectsUiState.asStateFlow()

    private val _newProjectUiState = MutableStateFlow<UiState>(UiState.Empty)
    val newProjectUiState = _newProjectUiState.asStateFlow()

    sealed class UiState {
        object Loading : UiState()
        class Error(val message: String) : UiState()
        object Empty : UiState()
        object Success : UiState()
    }

    fun fetchProjects() = viewModelScope.launch {
        if (projects.first().isEmpty()) _projectsUiState.value = UiState.Loading
        val response = projectRepository.fetchProjects()
        if (response.isSuccessful) {
            response.body()?.let {
                projectRepository.deleteProjects()
                projectRepository.insertProjects(it.data)
                _projectsUiState.value = UiState.Success
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

    fun resetNewProjectUiState() {
        _newProjectUiState.value = UiState.Empty
    }

    fun resetProjectsUiState() {
        _projectsUiState.value = UiState.Empty
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
        projectRepository.deleteProjects()
        projectRepository.deleteTasks()
    }
}