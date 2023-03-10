package com.example.mynotes.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "application")

class Preference(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[PreferenceKey.TOKEN] ?: ""
        }
    }

    suspend fun saveToken(message: String) {
        dataStore.edit { preference ->
            preference[PreferenceKey.TOKEN] = message
        }
    }

    fun getUsername(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[PreferenceKey.USERNAME] ?: ""
        }
    }

    suspend fun saveUsername(message: String) {
        dataStore.edit { preference ->
            preference[PreferenceKey.USERNAME] = message
        }
    }


    fun getEmailUser(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[PreferenceKey.EMAIL] ?: ""
        }
    }

    suspend fun saveEmailUser(message: String) {
        dataStore.edit { preference ->
            preference[PreferenceKey.EMAIL] = message
        }
    }

    suspend fun logout() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }
}