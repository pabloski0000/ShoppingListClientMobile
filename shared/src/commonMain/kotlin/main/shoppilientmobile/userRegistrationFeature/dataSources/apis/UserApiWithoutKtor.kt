package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.core.AsynchronousHttpClientImpl
import main.shoppilientmobile.core.apis.Api
import main.shoppilientmobile.core.localStorage.SecurityTokenKeeper
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole

class UserApiWithoutKtor(
    private val asynchronousHttpClient: AsynchronousHttpClientImpl,
    securityTokenKeeper: SecurityTokenKeeper,
): Api(securityTokenKeeper), UserApi {

    override suspend fun subscribeAdmin(user: User): Flow<JsonObject> {
        registerAdminUser(user)
        return sendAdminTheCodeToConfirmRegistration()
    }

    private suspend fun registerAdminUser(user: User) {
        val url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/register-user-admin"
        val headers = mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
        )
        val requestBody = """
            {
                "nickName": "${user.getNickname()}"
            }
        """.trimIndent()
        val response = asynchronousHttpClient.makeJsonPostRequest(url, headers, requestBody)
        val securityToken = response.getValue("accessToken").jsonPrimitive.content
        super.keepSecurityToken(securityToken)
    }

    private suspend fun sendAdminTheCodeToConfirmRegistration(): Flow<JsonObject> {
        val url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/alert-admin-to-confirm-registration"
        val headers = mapOf(
            "Authorization" to "Bearer ${super.getSecurityToken()}",
            "Accept" to "application/x-ndjson",
        )
        return asynchronousHttpClient.makeNDJsonGetRequest(url, headers).map { jsonObject ->
            println(jsonObject.toString())
            jsonObject
        }

    }

    override suspend fun registerBasicUser(user: User) {
        TODO("Not yet implemented")
    }
}