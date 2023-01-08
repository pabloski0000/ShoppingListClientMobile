package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.core.localStorage.SecurityTokenKeeper
import main.shoppilientmobile.core.remote.ShoppingListServerApi
import main.shoppilientmobile.core.remote.AsynchronousHttpClient
import main.shoppilientmobile.core.remote.HttpMethod
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.userRegistrationFeature.dataSources.NewUserRegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class NewUserRegistrationApi(
    private val httpClient: AsynchronousHttpClient,
    private val securityTokenKeeper: SecurityTokenKeeper,
): ShoppingListServerApi(), NewUserRegistrationRemoteDataSource {
    override suspend fun confirmUserRegistration(registration: Registration) {
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
        val responseBody = httpClient.makeRequest(httpRequest).body
        val json = Json.parseToJsonElement(responseBody).jsonObject
        securityTokenKeeper.setSecurityToken(json.getValue("accessToken").jsonPrimitive.content)
    }
}