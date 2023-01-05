package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.core.AsynchronousHttpClient
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.jsonStructures.JsonStructure
import startWithExceptions.UserCouldNotBeRegisteredException
import kotlin.reflect.KSuspendFunction1

class UserApiImpl(
    private val httpClient: HttpClient,
) {
    suspend fun registerAdminUser(user: User): SecurityToken {
        val requestBody = JsonStructure.UserRegistration(user.getNickname())
        val response = runPostRequest(
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/register-user-admin",
            requestBody = requestBody,
        )
        val statusCode = response.status
        if (! isStatusCodeIn2XXRange(statusCode))
            throwExceptionToInformClientOfUnregisteredUser(
                buildDefaultMessageToInformClient(
                    affectedEntity = "User",
                    statusCode = statusCode.value,
                    serverErrorResposne = interpretJsonObject(response),
                )
            )
        return interpretJsonObject<JsonStructure.SecurityToken>(response).accessToken
    }

    suspend fun registerBasicUser(user: User) {
        val requestBody = JsonStructure.UserRegistration(user.getNickname())
        val response = runPostRequest(
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/register-user",
            requestBody = requestBody,
        )
        val statusCode = response.status
        if (! isStatusCodeIn2XXRange(statusCode)){
            throwExceptionToInformClientOfUnregisteredUser(
                buildDefaultMessageToInformClient(
                    affectedEntity = "User",
                    statusCode = statusCode.value,
                    serverErrorResposne = interpretJsonObject(response),
                )
            )
        }
    }

    private fun throwExceptionToInformClientOfUnregisteredUser(
        message: String = "User could not be registered"
    ) {
        throw UserCouldNotBeRegisteredException(message)
    }

    private fun buildDefaultMessageToInformClient(
        affectedEntity: String,
        statusCode: Int,
        serverErrorResposne: JsonStructure.ServerErrorMessage
    ): String {
        return """
            $affectedEntity could not be added due to a server error. Error info:
                Status code: $statusCode
                Server error message: $serverErrorResposne}
            """.trimIndent()
    }

    private fun runPostRequest(url: String, requestBody: JsonStructure, accessToken: String? = null): HttpResponse {
        return runRequest(buildRequestBuilder(url, requestBody, accessToken), ::sendPostRequest)
    }

    private fun buildRequestBuilder(url: String, requestBody: JsonStructure? = null, accessToken: String? = null): HttpRequestBuilder {
        val requestBuilder = HttpRequestBuilder()
        requestBuilder.url(url)
        if (accessToken != null){
            requestBuilder.headers {
                append("Authorization", "Bearer $accessToken")
            }
        }
        requestBuilder.header("Accept", "application/json")
        requestBuilder.header("Content-Type", "application/json")
        if (requestBody != null)
            requestBuilder.setBody(requestBody)

        return requestBuilder
    }

    private fun runRequest(
        httpRequestBuilder: HttpRequestBuilder,
        request: KSuspendFunction1<HttpRequestBuilder, HttpResponse>
    ): HttpResponse {
        return runBlocking { request(httpRequestBuilder) }
    }

    private suspend fun sendPostRequest(httpRequestBuilder: HttpRequestBuilder): HttpResponse {
        return httpClient.post(httpRequestBuilder)
    }

    private inline fun <reified T: JsonStructure> interpretJsonObject(response: HttpResponse): T{
        return runBlocking { response.body() }
    }

    private fun isStatusCodeIn2XXRange(statusCode: HttpStatusCode): Boolean{
        return statusCode.value in 200..299
    }
}