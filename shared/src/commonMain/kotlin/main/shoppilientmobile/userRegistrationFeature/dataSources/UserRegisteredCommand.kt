package main.shoppilientmobile.userRegistrationFeature.dataSources

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.jsonStructures.JsonStructure

class UserRegisteredCommand(
    private val host: String,
    private val httpClient: HttpClient,
    private val httpRequestBuilder: HttpRequestBuilder,
    private val userNickname: String,
) {
    private val resourcePath = "api/users/register-user"

    fun execute() {
        runBlocking {
            httpClient.request(buildRequest())
        }
    }

    private fun buildRequest(): HttpRequestBuilder {
        httpRequestBuilder.url("$host/$resourcePath")
        httpRequestBuilder.method = HttpMethod.Post
        httpRequestBuilder.headers {
            append("Accept", "*/*")
            append("Content-Type", "application/json")
        }
        httpRequestBuilder.setBody(buildBody())
        return httpRequestBuilder
    }

    private fun buildBody() = JsonStructure.UserRegistration(nickName = userNickname)
}