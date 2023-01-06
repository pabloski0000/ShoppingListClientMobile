package main.shoppilientmobile.dataSources

import io.ktor.client.engine.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import main.shoppilientmobile.core.AsynchronousHttpClientImpl
import java.net.HttpURLConnection
import java.net.URL

class AsynchronousHttpClientAndroid(
    httpClientEngine: HttpClientEngine? = null
): AsynchronousHttpClientImpl(httpClientEngine) {
    override suspend fun makeNDJsonGetRequest(
        url: String,
        headers: Map<String, String>
    ): Flow<JsonObject> {
        val httpConnection = URL(url).openConnection() as HttpURLConnection
        httpConnection.setRequestMethod("GET")
        headers.map {
            httpConnection.setRequestProperty(it.key, it.value)
        }
        val inputStreamBufferedReader = httpConnection.inputStream.bufferedReader()
        return flow {
            inputStreamBufferedReader.lineSequence().forEach { responseLine ->
                emit(Json.parseToJsonElement(responseLine).jsonObject)
            }
        }
    }

}