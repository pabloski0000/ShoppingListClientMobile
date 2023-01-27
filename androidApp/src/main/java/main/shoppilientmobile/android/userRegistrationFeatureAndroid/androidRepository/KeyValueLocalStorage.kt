package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.exceptions.NotFoundKeyException
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.exceptions.StorageException
import java.util.*
import kotlin.reflect.KClass

class KeyValueLocalStorage(
    private val dataStore: DataStore<Preferences>
) {
    private var saving = false

    @kotlin.jvm.Throws(StorageException::class)
    suspend fun store(value: String): Key {
        val key = getUniqueKey()
        try {
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
            return key
        } catch (e: Exception) {
            throw StorageException("Error at storing data as Key-Value")
        }
    }

    @kotlin.jvm.Throws(NotFoundKeyException::class)
    suspend fun getValue(key: String): String {
        try {
            return coroutineScope {
                val value = async {
                    delayUntilNothingIsBeingSaved()
                        dataStore.data.map { preferences ->
                            preferences[stringPreferencesKey(key)]
                        }.first() ?: ""
                }
                return@coroutineScope value.await()
            }
        } catch (e: Exception) {
            throw NotFoundKeyException("Key: $key does not exist in storage")
        }
    }

    private fun delayUntilNothingIsBeingSaved() {
        while (saving) {}
    }

    fun <T: Any> canBeStoredAsKeyValue(objectToStore: T): Boolean {
        return try {
            TODO("Let's implement Room first")
            true
        } catch (e: CannotBuildPreferencesKeyForSuchATypeException) {
            false
        }
    }

    private fun getUniqueKey(): String = UUID.randomUUID().toString()

    private class CannotBuildPreferencesKeyForSuchATypeException(message: String): RuntimeException(message)
}