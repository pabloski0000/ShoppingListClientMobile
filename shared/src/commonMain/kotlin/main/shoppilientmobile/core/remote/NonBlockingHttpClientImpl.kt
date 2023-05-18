package main.shoppilientmobile.core.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toMap
import io.ktor.utils.io.core.EOFException
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json

class NonBlockingHttpClientImpl(
    httpClientEngineFactory: HttpClientEngineFactory<HttpClientEngineConfig>? = null,
): NonBlockingHttpClient, StreamingHttpClient {
    private val httpClient: HttpClient
    private var numberOfOpenConnections = 0
    init {
        val httpConfiguration: HttpClientConfig<*>.() -> Unit = {
            install(HttpTimeout) {
                socketTimeoutMillis = Long.MAX_VALUE
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }

        httpClient = if (httpClientEngineFactory == null) {
            println("EngineFactory = CIO")
            HttpClient(CIO, httpConfiguration)
        } else {
            println("EngineFactory = DIFFERENT")
            HttpClient(httpClientEngineFactory, httpConfiguration)
        }
    }

    override suspend fun makeRequest(httpRequest: HttpRequest): HttpResponse {
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
        return HttpResponse(
            statusCode = response.status.value,
            headers = response.headers.toMap(),
            body = response.bodyAsText(),
        )
    }

    override suspend fun makeStreamingRequest(httpRequest: HttpRequest): Flow<String> {
        return flow {
            try {
                println("Number of open connections: ${++numberOfOpenConnections}")
                httpClient.prepareRequest {
                    url(httpRequest.url)
                    method = adaptHttpMethod(httpRequest.httpMethod)
                    headers {
                        httpRequest.headers.map { header ->
                            append(header.key, header.value)
                        }
                    }
                    setBody(httpRequest.body)
                }.execute { response ->
                    val responseChannel = response.bodyAsChannel()
                    while (true) {
                        yield()
                        val receivedChunk = StringBuilder()
                        responseChannel.awaitContent()
                        while (responseChannel.availableForRead > 0) {
                            receivedChunk.append(responseChannel.readUTF8Line())
                            receivedChunk.append("\n")
                        }
                        emit(receivedChunk.toString())
                    }
                }
            } finally {
                println("Number of open connections: ${--numberOfOpenConnections}")
            }
        }
    }

    override suspend fun stopStreamingRequest() {

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

