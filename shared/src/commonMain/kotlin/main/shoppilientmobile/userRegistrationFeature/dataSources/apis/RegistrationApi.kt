package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.core.localStorage.SecurityTokenKeeper
import main.shoppilientmobile.core.remote.*
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.RegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegistrationApi(
    private val httpClient: AsynchronousHttpClient,
    private val userBuilder: UserBuilder,
    private val securityTokenKeeper: SecurityTokenKeeper,
): ShoppingListServerApi(), RegistrationRemoteDataSource {
    override suspend fun registerAdmin(registration: Registration): User {
        signUpAdmin(registration)
        return createUser(registration)
    }

    override suspend fun registerUser(registration: Registration) {
        signUpUser(registration)
    }

    private suspend fun signUpUser(registration: Registration) {
        val url = "$protocolAndHost/api/users/register-user"
        val headers = mapOf(
            "Content-Type" to "application/json",
        )
        val body = """
            {"nickname": "${registration.nickname}"}
        """.trimIndent()
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = headers,
            body = body,
        )
        httpClient.makeRequest(httpRequest)
    }

    private suspend fun signUpAdmin(registration: Registration) {
        val url = "$protocolAndHost/api/users/register-user-admin"
        val headers = mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
        )
        val body = """
            {"nickname": "${registration.nickname}"}
        """.trimIndent()
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = headers,
            body = body,
        )
        val response = httpClient.makeRequest(httpRequest)
        if (response.statusCode !in 200..299) {
            throw RemoteDataSourceException("Server failed at taking registration")
        }
        saveSecurityToken(response)
    }

    override suspend fun confirmUserRegistration(registration: Registration): User {
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = "$protocolAndHost/api/users/confirm-registration",
            headers = mapOf("Content-Type" to "application/json"),
            body = """
                {
                    "nickname": "${registration.nickname}",
                    "code": ${registration.signature}
                }
            """.trimIndent(),
        )
        val response = httpClient.makeRequest(httpRequest)
        if (response.statusCode !in 200..299) {
            throw RemoteDataSourceException("Server failed at taking the registration")
        }
        saveSecurityToken(response)
        return createUser(registration)
    }

    private suspend fun saveSecurityToken(response: HttpResponse) {
        val responseBody = response.body
        val json = Json.parseToJsonElement(responseBody).jsonObject
        val securityToken = json.getValue("accessToken").jsonPrimitive.content
        securityTokenKeeper.setSecurityToken(securityToken)
    }

    private fun createUser(registration: Registration): User {
        return userBuilder.giveItANickname(registration.nickname).setRole(registration.role).build()
    }
}