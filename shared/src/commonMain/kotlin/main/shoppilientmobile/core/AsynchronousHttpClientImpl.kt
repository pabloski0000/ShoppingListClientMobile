package main.shoppilientmobile.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.*

class AsynchronousHttpClientImpl(
    httpClientEngine: HttpClientEngine? = null
): AsynchronousHttpClient {
    private val httpClient: HttpClient
    init {
        val jsonParserAndDebugLog: HttpClientConfig<*>.() -> Unit = {
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
            HttpClient(engineFactory = CIO, jsonParserAndDebugLog)
        } else {
            HttpClient(engine = httpClientEngine, jsonParserAndDebugLog)
        }
    }

    suspend fun makeNDJsonGetRequest(url: String, headers: Map<String, String>): Flow<JsonObject> {
        return httpClient.prepareGet(
            block = {
                url(url)
                headers {
                    headers.map { append(it.key, it.value) }
                }
            }
        ).execute { httpResponse ->
            return@execute flow<JsonObject> {
                val byteReadChannel = httpResponse.body<ByteReadChannel>()
                val jsonObjectAsString = StringBuilder()
                while (! byteReadChannel.isClosedForRead) {
                    val packet = byteReadChannel.readRemaining(DEFAULT_HTTP_BUFFER_SIZE.toLong())
                    packet.readBytes().map { jsonSymbol ->
                        jsonObjectAsString.append(jsonSymbol)
                        if (isJsonObject(jsonObjectAsString.toString())) {
                            println(jsonObjectAsString.toString())
                            emit(Json.parseToJsonElement(jsonObjectAsString.toString()).jsonObject)
                            jsonObjectAsString.clear()
                        }
                    }
                }
            }
        }
    }

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
}

