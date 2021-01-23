package com.example.tasks.data.repositories

import com.example.tasks.data.AuthHelpers
import com.example.tasks.data.models.User
import com.example.tasks.data.network.AuthAPI
import com.example.tasks.data.network.interceptors.ProjectApiInterceptor
import com.example.tasks.data.network.payloads.LoginPayload
import com.example.tasks.data.network.responses.AuthResponse
import com.example.tasks.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthRepository(
    private val authAPI: AuthAPI,
    private val authHelpers: AuthHelpers,
    private val projectApiInterceptor: ProjectApiInterceptor
) {

    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    suspend fun logout() {
        user = null
        authHelpers.clearStore()
    }

    suspend fun checkAuth(): Boolean {
        val token = authHelpers.getToken()
        return token?.let {
            val user = authHelpers.getUser()
            user?.let {
                authenticate(AuthResponse(data = user, token = token, success = true))
                true
            } ?: false
        } ?: false
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun fetchUser(): Result<User> = withContext(Dispatchers.IO) {
        val token = authHelpers.getToken()
        token?.let {
            val result = authAPI.getUser(token)
            return@withContext if (result.isSuccessful) {
                val response = result.body()!!
                response.token = token
                authenticate(response)
                save(response)
                Result.Success(data = result.body()!!.data)
            } else try {
                result.errorBody()?.string()?.let {
                    Result.Error(JSONObject(it).getString("message"))
                } ?: Result.Error("Could not get user")
            } catch (e: Exception) {
                Result.Error("Could not get user")
            }
        } ?: Result.Error("No token found")
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val payload = LoginPayload(email, password)
        val result = authAPI.login(payload)
        return@withContext if (result.isSuccessful) {
            val response = result.body()!!
            authenticate(response)
            save(response)
            Result.Success(data = result.body()!!.data)
        } else try {
            result.errorBody()?.string()?.let {
                Result.Error(JSONObject(it).getString("message"))
            } ?: Result.Error("Could not login, please try again")
        } catch (e: Exception) {
            Result.Error("Please check your email and try again")
        }
    }

    private fun authenticate(authResponse: AuthResponse) {
        projectApiInterceptor.setToken(authResponse.token)
        this.user = authResponse.data
    }

    private suspend fun save(authResponse: AuthResponse) {
        authHelpers.saveToken(authResponse.token)
        authHelpers.saveUser(authResponse.data)
    }
}
