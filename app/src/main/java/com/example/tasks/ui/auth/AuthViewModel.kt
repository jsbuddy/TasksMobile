package com.example.tasks.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.tasks.data.repositories.AuthRepository

class AuthViewModel @ViewModelInject() constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val user = authRepository.user

    suspend fun checkAuth() = authRepository.checkAuth()

    suspend fun logout() = authRepository.logout()
}