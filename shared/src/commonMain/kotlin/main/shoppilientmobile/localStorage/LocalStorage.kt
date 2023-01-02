package main.shoppilientmobile.localStorage

expect class LocalStorage {
    suspend fun save(key: String, value: String)
    suspend fun getValue(key: String): String
}