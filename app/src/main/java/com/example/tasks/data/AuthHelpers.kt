package com.example.tasks.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.example.tasks.data.models.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class AuthHelpers(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "auth_store")

    fun getToken() = runBlocking { dataStore.data.map { p -> p[AUTH_TOKEN_KEY] }.firstOrNull() }

    suspend fun saveToken(token: String) = dataStore.edit { s -> s[AUTH_TOKEN_KEY] = token }

    suspend fun saveUser(user: User) = dataStore.edit { s ->
        s[USER_ID_KEY] = user.id
        s[USER_NAME_KEY] = user.name
        s[USER_EMAIL_KEY] = user.email
    }

    suspend fun getUser(): User? = dataStore.data.map { s ->
        val user = User(
            id = s[USER_ID_KEY] ?: "",
            name = s[USER_NAME_KEY] ?: "",
            email = s[USER_EMAIL_KEY] ?: "",
        )
        return@map if (user.id.isNotBlank()) user else null
    }.firstOrNull()

    suspend fun clearStore() = dataStore.edit { store -> store.clear() }

    companion object {
        val AUTH_TOKEN_KEY = stringPreferencesKey("AUTH_TOKEN")
        val USER_ID_KEY = stringPreferencesKey("ID")
        val USER_NAME_KEY = stringPreferencesKey("NAME")
        val USER_EMAIL_KEY = stringPreferencesKey("EMAIL")
    }
}
