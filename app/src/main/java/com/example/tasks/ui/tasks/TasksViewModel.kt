package com.example.tasks.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.network.payloads.UpdateTaskPayload
import com.example.tasks.data.repositories.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    lateinit var project: Project

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState = _uiState.asStateFlow()

    fun tasks() = repository.getTasks(project.id)

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        class Error(val message: String) : UiState()
        object Empty : UiState()
    }

    fun initialize(project: Project) {
        this.project = project
        this.fetchTasks()
    }

    private fun fetchTasks() = viewModelScope.launch {
        val response = repository.fetchTasks(project.id)
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertTasks(it.data)
                _uiState.value = UiState.Success
            }
        }
    }

    fun toggleCompleted(id: String, completed: Boolean) = viewModelScope.launch {
        val payload = UpdateTaskPayload(completed = completed)
        val response = repository.updateTask(id, payload)
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertTask(it.data)
            }
        }
    }
}