package com.example.tasks.ui.auth

import androidx.lifecycle.ViewModel
import com.example.tasks.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val user = authRepository.user

    suspend fun checkAuth() = authRepository.checkAuth()

    suspend fun logout() = authRepository.logout()
}