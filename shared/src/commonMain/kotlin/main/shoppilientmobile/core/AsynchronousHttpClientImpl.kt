package main.shoppilientmobile.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.*

abstract class AsynchronousHttpClientImpl(
    httpClientEngine: HttpClientEngine?
): AsynchronousHttpClient {
    private val httpClient: HttpClient
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

    abstract suspend fun makeNDJsonGetRequest(url: String, headers: Map<String, String>): Flow<JsonObject>

    private fun isJsonObject(string: String): Boolean {
        return string.matches("^\\{(\\n)?( )*\"nickName\":( )?\"\\w{0,15}\"(\\n)?}${'$'}".toRegex())
    }

    suspend fun makeJsonPostRequest(url: String, headers: Map<String, String>, body: String): JsonObject {
        val response = httpClient.post {
            url(url)
            headers {
                headers.map { append(it.key, it.value) }
            }
            setBody(body)
        }.bodyAsText()
        return Json.parseToJsonElement(response).jsonObject
    }

    protected fun getHttpClient() = httpClient
}

