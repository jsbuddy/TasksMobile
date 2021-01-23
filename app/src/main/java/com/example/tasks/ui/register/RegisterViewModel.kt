package com.example.tasks.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.R
import com.example.tasks.data.repositories.AuthRepository
import com.example.tasks.utils.Result
import com.example.tasks.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Nothing)
    val uiState = _uiState.asStateFlow()

    sealed class UiState {
        object Nothing : UiState()
        object Loading : UiState()

        class FormError(
            val name: Int? = null,
            val email: Int? = null,
            val password: Int? = null,
            val confirmPassword: Int? = null,
            val valid: Boolean = false,
        ) : UiState()

        class Error(val message: String) : UiState()
        class Success(val message: Int) : UiState()
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        val result = authRepository.register(name, email, password)
        if (result is Result.Success) {
            _uiState.value = UiState.Success(message = R.string.login_successful)
        } else if (result is Result.Error) {
            _uiState.value = UiState.Error(message = result.message)
        }
    }

    fun registerDataChanged(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (name.isBlank() || name.length < 3) {
            _uiState.value = UiState.FormError(email = R.string.invalid_name)
        } else if (!Utils.isEmailValid(email)) {
            _uiState.value = UiState.FormError(email = R.string.invalid_email)
        } else if (!Utils.isPasswordValid(password)) {
            _uiState.value = UiState.FormError(password = R.string.invalid_password)
        } else if (password != confirmPassword) {
            _uiState.value = UiState.FormError(confirmPassword = R.string.invalid_confirm_password)
        } else {
            _uiState.value = UiState.FormError(valid = true)
        }
    }
}