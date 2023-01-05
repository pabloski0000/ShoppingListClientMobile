package main.shoppilientmobile.core.localStorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import main.shoppilientmobile.core.localStorage.exceptions.NotFoundKeyException

actual class KeyValueLocalStorage(
    private val dataStore: DataStore<Preferences>
) {
    private var saving = false

    actual suspend fun put(key: String, value: String) {
        coroutineScope {
            launch {
                try {
                    saving = true
                    dataStore.edit { mutablePreferences ->
                        mutablePreferences[stringPreferencesKey(key)] = value
                    }
                } finally {
                    saving = false
                }
            }
        }
    }

    actual suspend fun getValue(key: String): String {
        return coroutineScope {
            val value = async {
                delayUntilNothingIsBeingSaved()
                try {
                    dataStore.data.map { preferences ->
                        preferences[buildPreferencesKey(key)]
                    }.first() ?: ""
                } catch (e: Exception) {
                    throw NotFoundKeyException("Key: $key does not exist in storage")
                }
            }
            return@coroutineScope value.await()
        }
    }

    private fun delayUntilNothingIsBeingSaved() {
        while (saving) {}
    }

    private fun buildPreferencesKey(key: String) = stringPreferencesKey(key)
}