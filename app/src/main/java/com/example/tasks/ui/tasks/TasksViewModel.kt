package com.example.tasks.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.db.entities.Project
import com.example.tasks.data.db.entities.Task
import com.example.tasks.data.network.payloads.CreateTaskPayload
import com.example.tasks.data.network.payloads.UpdateTaskPayload
import com.example.tasks.data.repositories.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val repository: ProjectRepository,
) : ViewModel() {
    lateinit var project: Project

    private val _tasksUiState = MutableStateFlow<UiState>(UiState.Empty)
    val tasksUiState = _tasksUiState.asStateFlow()

    private val _newTaskUiState = MutableStateFlow<UiState>(UiState.Empty)
    val newTaskUiState = _newTaskUiState.asStateFlow()

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
                _tasksUiState.value = UiState.Success
            }
        }
    }

    fun toggleCompleted(id: String, completed: Boolean) = viewModelScope.launch {
        val payload = UpdateTaskPayload(completed = completed)
        val response = repository.updateTask(id, payload)
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertTask(it.data)
                fetchProject(project.id)
            }
        }
    }

    private fun fetchProject(id: String) = viewModelScope.launch {
        val response = repository.fetchProject(id)
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertProject(it.data)
            }
        }
    }

    fun createTask(name: String, priority: Int, due: String) = viewModelScope.launch {
        _newTaskUiState.value = UiState.Loading
        val payload = CreateTaskPayload(name, priority, due, project.id)
        val response = repository.createTask(payload)
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertTask(it.data)
                _newTaskUiState.value = UiState.Success
                fetchProject(project.id)
            }
        } else {
            _newTaskUiState.value = UiState.Error("Unable to create task, please try again")
        }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task.id)
        repository.deleteTask(task)
        fetchProject(project.id)
    }

    fun deleteProject() = viewModelScope.launch {
        repository.deleteProjectAndTasks(project)
        repository.deleteProject(project.id)
    }

    fun resetNewTaskState() {
        _newTaskUiState.value = UiState.Empty
    }
}