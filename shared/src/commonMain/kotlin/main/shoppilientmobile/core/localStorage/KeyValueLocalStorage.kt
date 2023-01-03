package main.shoppilientmobile.core.localStorage

expect class KeyValueLocalStorage {
    suspend fun put(key: String, value: String)
    suspend fun getValue(key: String): String
}