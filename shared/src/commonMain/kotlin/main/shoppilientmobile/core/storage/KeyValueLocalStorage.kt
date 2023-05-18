package main.shoppilientmobile.core.storage

interface KeyValueLocalStorage {
    suspend fun store(key: String, value: String)
    suspend fun getValue(key: String): String
}