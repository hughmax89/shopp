package com.aguerodev.shopp.view.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aguerodev.shopp.domain.entity.Country // Asegúrate de importar tu clase/enum Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val SHOULD_REMEMBER_KEY = booleanPreferencesKey("should_remember")
    private val SELECTED_COUNTRY_KEY = stringPreferencesKey("selected_country")

    suspend fun saveUserCountry(email: String, remember: Boolean, country: Country) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
            preferences[SHOULD_REMEMBER_KEY] = remember
            preferences[SELECTED_COUNTRY_KEY] = country.name
        }
    }

    suspend fun clearAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val userPreferencesFlow: Flow<Triple<String?, Boolean, Country?>> = dataStore.data
        .map { preferences ->
            val email = preferences[USER_EMAIL_KEY]
            val shouldRemember = preferences[SHOULD_REMEMBER_KEY] ?: false


            val countryString = preferences[SELECTED_COUNTRY_KEY]

            val country = if (countryString != null) {
                try {
                    Country.valueOf(countryString)
                } catch (e: IllegalArgumentException) {
                    Country.COUNTRY_A
                }
            } else {
                null
            }
            Triple(email, shouldRemember, country)
        }
}