import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import main.shoppilientmobile.domain.User
import main.shoppilientmobile.httpBodyStructures.JsonStructure
import startWithExceptions.UserCouldNotBeRegisteredException
import kotlin.reflect.KSuspendFunction1

class CustomHttpClient(
    httpClientEngine: HttpClientEngine? = null
){
    private val httpClient: HttpClient
    private var accessToken: String? = null

    init {
        httpClient = if (httpClientEngine == null){
            buildHttpClientWithoutEngine()
        } else {
            buildHttpClientWithEngine(httpClientEngine)
        }
    }

    fun buildHttpClientWithoutEngine(): HttpClient{
        return HttpClient() {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    fun buildHttpClientWithEngine(engine: HttpClientEngine): HttpClient{
        return HttpClient(engine){
            install(ContentNegotiation){
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    fun registerUser(user: User) {
        val requestBody = JsonStructure.UserRegistration(user.getNickname())
        val response = runPostRequest(
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/register-user-admin",
            requestBody = requestBody,
        )
        val statusCode = response.status
        if (isStatusCodeIn2XXRange(statusCode))
            accessToken = interpretJsonObject<JsonStructure.SecurityToken>(response).accessToken
        else
            throw UserCouldNotBeRegisteredException("""
                    Admin could not be registered due to a server error. Error info:
                        Status code: $statusCode
                        Server error message: ${interpretJsonObject<JsonStructure.ServerErrorMessage>(response)}
                """.trimIndent())
    }

    fun getAllProducts(accessToken: String): List<JsonStructure.Product> {
        val response = runGetRequest(
            "https://lista-de-la-compra-pabloski.herokuapp.com/api/products",
            accessToken)
        return interpretJsonList(response)
    }

    fun addProduct(accessToken: String, requestBody: JsonStructure.ProductAddition): JsonStructure.Product{
        val createdProductResponse: JsonStructure.Product
        val response = runPostRequest(
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products",
            requestBody = requestBody,
            accessToken = accessToken
        )
        val statusCode = response.status
        if (isStatusCodeIn2XXRange(statusCode))
            createdProductResponse = interpretJsonObject(response)
        else
            throw UserCouldNotBeRegisteredException("""
                    Product could not be added due to a server error. Error info:
                        Status code: ${statusCode.value}
                        Server error message: ${interpretJsonObject<JsonStructure.ServerErrorMessage>(response)}
                """.trimIndent())
        return createdProductResponse
    }

    fun modifyProduct(accessToken: String, productId: String, newProduct: JsonStructure.ProductAddition) {
        val url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products/$productId"
        runPutRequest(url, newProduct, accessToken)
    }

    private fun runPostRequest(url: String, requestBody: JsonStructure, accessToken: String? = null): HttpResponse{
        return runRequest(buildRequestBuilder(url, requestBody, accessToken), ::sendPostRequest)
    }

    private fun runGetRequest(url: String, accessToken: String? = null): HttpResponse{
        return runRequest(buildRequestBuilder(url, null, accessToken), ::sendGetRequest)
    }

    private fun runPutRequest(url: String, requestBody: JsonStructure, accessToken: String? = null): HttpResponse{
        return runRequest(buildRequestBuilder(url, requestBody, accessToken), ::sendPutRequest)
    }

    private fun buildRequestBuilder(url: String, requestBody: JsonStructure? = null, accessToken: String? = null): HttpRequestBuilder{
        val requestBuilder = HttpRequestBuilder()
        requestBuilder.url(url)
        if (accessToken != null){
            requestBuilder.headers {
                append("Authorization", "Bearer $accessToken")
            }
        }
        requestBuilder.header("Accept", "*/*")
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

    private suspend fun sendGetRequest(httpRequestBuilder: HttpRequestBuilder): HttpResponse{
        return httpClient.get(httpRequestBuilder)
    }
    private suspend fun sendPostRequest(httpRequestBuilder: HttpRequestBuilder): HttpResponse{
        return httpClient.post(httpRequestBuilder)
    }
    private suspend fun sendPutRequest(httpRequestBuilder: HttpRequestBuilder): HttpResponse{
        return httpClient.put(httpRequestBuilder)
    }

    private inline fun <reified T: JsonStructure> interpretJsonObject(response: HttpResponse): T{
        return runBlocking { response.body() }
    }
    private inline fun <reified T: List<JsonStructure>> interpretJsonList(response: HttpResponse): T{
        return runBlocking { response.body() }
    }

    private fun isStatusCodeIn2XXRange(statusCode: HttpStatusCode): Boolean{
        return statusCode.value in 200..299
    }
}

