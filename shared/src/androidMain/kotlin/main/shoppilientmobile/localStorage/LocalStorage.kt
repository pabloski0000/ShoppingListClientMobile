package main.shoppilientmobile.localStorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

actual class LocalStorage(
    private val dataStore: DataStore<Preferences>
) {
    actual suspend fun save(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    actual suspend fun getValue(key: String): String {
        return try {
            dataStore.data.map { preferences ->
                preferences[buildPreferencesKey(key)]
            }.first() ?: ""
        } catch (e: Exception) {
            throw NotFoundKeyException("Key: $key does not exist in storage")
        }
    }

    private fun buildPreferencesKey(key: String) = stringPreferencesKey(key)
}