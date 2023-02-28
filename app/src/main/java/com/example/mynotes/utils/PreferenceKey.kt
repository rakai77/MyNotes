package com.example.mynotes.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKey {

    val TOKEN = stringPreferencesKey("token")
    val USERNAME = stringPreferencesKey("username")
    val EMAIL = stringPreferencesKey("email")

}