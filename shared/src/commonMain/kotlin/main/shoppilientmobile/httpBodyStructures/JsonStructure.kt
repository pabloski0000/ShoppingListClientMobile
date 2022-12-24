package main.shoppilientmobile.httpBodyStructures

sealed class JsonStructure {
    @kotlinx.serialization.Serializable
    data class UserRegistration(val nickName: String): JsonStructure()
    @kotlinx.serialization.Serializable
    data class ProductAddition(val name: String): JsonStructure()
    @kotlinx.serialization.Serializable
    data class Product(val id: String, val name: String): JsonStructure()
    @kotlinx.serialization.Serializable
    data class SecurityToken(val accessToken: String): JsonStructure()
    @kotlinx.serialization.Serializable
    data class ServerErrorMessage(val errorMessage: String): JsonStructure()
}