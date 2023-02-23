package main.shoppilientmobile.core.remote

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*

class AsynchronousHttpClientImpl(
    httpClientEngine: HttpClientEngine? = null
): AsynchronousHttpClient {
    private val httpClient: HttpClient
    private val coroutineScopeOnMainThread = CoroutineScope(Dispatchers.Main)
    init {
        val httpConfiguration: HttpClientConfig<*>.() -> Unit = {
            install(HttpTimeout) {
                requestTimeoutMillis = 55000
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
        httpClient = if (httpClientEngine == null) {
            HttpClient(engineFactory = CIO, httpConfiguration)
        } else {
            HttpClient(engine = httpClientEngine, httpConfiguration)
        }
    }

    override suspend fun makeRequest(httpRequest: HttpRequest): main.shoppilientmobile.core.remote.HttpResponse {
        val response = httpClient.request {
            url(httpRequest.url)
            method = adaptHttpMethod(httpRequest.httpMethod)
            headers {
                httpRequest.headers.map { header ->
                    append(header.key, header.value)
                }
            }
            setBody(httpRequest.body)
        }
        return main.shoppilientmobile.core.remote.HttpResponse(
            statusCode = response.status.value,
            headers = response.headers.toMap(),
            body = response.bodyAsText(),
        )
    }

    fun makeRequest2(httpRequest: HttpRequest): main.shoppilientmobile.core.remote.HttpResponse {
        return runBlocking(Dispatchers.Default) {
            val response: io.ktor.client.statement.HttpResponse
            coroutineScope {
                response = httpClient.request {
                    url(httpRequest.url)
                    method = adaptHttpMethod(httpRequest.httpMethod)
                    headers {
                        httpRequest.headers.map { header ->
                            append(header.key, header.value)
                        }
                    }
                    setBody(httpRequest.body)
                }
                main.shoppilientmobile.core.remote.HttpResponse(
                    statusCode = response.status.value,
                    headers = response.headers.toMap(),
                    body = response.bodyAsText(),
                )
            }
        }
    }

    private fun adaptHttpMethod(httpMethod: HttpMethod): io.ktor.http.HttpMethod {
        return when (httpMethod) {
            HttpMethod.GET -> io.ktor.http.HttpMethod.Get
            HttpMethod.POST -> io.ktor.http.HttpMethod.Post
            HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
            HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
            HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
        }
    }
}

