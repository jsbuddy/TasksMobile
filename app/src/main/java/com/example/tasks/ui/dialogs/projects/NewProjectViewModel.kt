package com.example.tasks.ui.dialogs.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.ProjectRepository
import com.example.tasks.data.network.payloads.CreateProjectPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewProjectViewModel @ViewModelInject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    sealed class UiState {
        object Loading : UiState()
        class Error(val message: String) : UiState()
        object Empty : UiState()
        object Success : UiState()
    }

    fun create(name: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        val response = repository.createProject(CreateProjectPayload(name))
        if (response.isSuccessful) {
            response.body()?.let {
                repository.insertProject(it.data)
                _uiState.value = UiState.Success
            }
        } else {
            _uiState.value = UiState.Error("Unable to create project, please try again")
        }
    }
}