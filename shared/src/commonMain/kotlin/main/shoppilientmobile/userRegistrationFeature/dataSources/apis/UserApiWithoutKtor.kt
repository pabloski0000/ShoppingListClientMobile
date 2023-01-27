package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.core.remote.*
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class UserApiWithoutKtor(
    private val httpClient: AsynchronousHttpClient,
    private val streamingHttpClient: StreamingHttpClient,
    private val securityTokenKeeper: SecurityTokenKeeper,
): ShoppingListServerApi(), UserRemoteDataSource {

    override suspend fun registerAdmin(user: User): AdminRegistration {
        signUpAdmin(user)
        return AdminRegistration(
            futureUserRegistrations = listenToFutureUserRegistrations()
        )
    }

    override suspend fun registerUser(user: User) {
        signUpUser(user)
    }

    private suspend fun signUpAdmin(user: User) {
        val url = "$protocolAndHost/api/users/register-user-admin"
        val headers = mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
        )
        val body = """
            {"nickname": "${user.getNickname()}"}
        """.trimIndent()
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = headers,
            body = body,
        )
        val responseBody = httpClient.makeRequest(httpRequest).body
        val json = Json.parseToJsonElement(responseBody).jsonObject
        val securityToken = json.getValue("accessToken").jsonPrimitive.content
        securityTokenKeeper.setSecurityToken(securityToken)
    }

    private suspend fun signUpUser(user: User) {
        val url = "$protocolAndHost/api/users/register-user"
        val headers = mapOf(
            "Content-Type" to "application/json",
        )
        val body = """
            {"nickname": "${user.getNickname()}"}
        """.trimIndent()
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = headers,
            body = body,
        )
        httpClient.makeRequest(httpRequest)
    }

    private suspend fun listenToFutureUserRegistrations(): Flow<Registration> {
        val url = "$protocolAndHost/api/users/alert-admin-to-confirm-registration"
        val headers = mapOf(
            "Authorization" to "Bearer ${securityTokenKeeper.getSecurityToken()}",
            "Accept" to "application/x-ndjson",
        )
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.GET,
            url = url,
            headers = headers,
            body = ""
        )
        return streamingHttpClient.makeRequest(httpRequest).map { chunk ->
            val json = Json.parseToJsonElement(chunk).jsonObject
            Registration(
                nickname = json.getValue("nickname").jsonPrimitive.content,
                role = UserRole.BASIC,
                signature = json.getValue("code").jsonPrimitive.int.toString(),
            )
        }
    }
}