package com.aguerodev.shopp.view.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // Definición de las claves
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val SHOULD_REMEMBER_KEY = booleanPreferencesKey("should_remember")

    suspend fun saveUserEmail(email: String, remember: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
            preferences[SHOULD_REMEMBER_KEY] = remember
        }
    }

    val userPreferencesFlow: Flow<Pair<String?, Boolean>> = dataStore.data
        .map { preferences ->
            val email = preferences[USER_EMAIL_KEY]
            val shouldRemember = preferences[SHOULD_REMEMBER_KEY] ?: false
            Pair(email, shouldRemember)
        }
}