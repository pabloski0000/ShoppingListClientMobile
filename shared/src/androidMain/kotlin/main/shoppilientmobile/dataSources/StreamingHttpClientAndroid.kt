package main.shoppilientmobile.dataSources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import main.shoppilientmobile.core.remote.HttpMethod
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.core.remote.StreamingHttpClient
import java.net.HttpURLConnection
import java.net.URL

class StreamingHttpClientAndroid: StreamingHttpClient {

    override suspend fun makeRequest(httpRequest: HttpRequest): Flow<String> {
        val httpConnection = URL(httpRequest.url).openConnection() as HttpURLConnection
        httpConnection.requestMethod = adaptHttpMethod(httpRequest.httpMethod)
        httpRequest.headers.map { header ->
            httpConnection.setRequestProperty(header.key, header.value)
        }
        val inputStreamBufferedReader = httpConnection.inputStream.bufferedReader()
        return flow {
            inputStreamBufferedReader.lineSequence().forEach { responseLine ->
                println("Server response: $responseLine")
                emit(responseLine)
            }
        }
    }

    private fun adaptHttpMethod(httpMethod: HttpMethod): String {
        return when (httpMethod) {
            HttpMethod.GET -> "GET"
            HttpMethod.POST -> "POST"
            HttpMethod.PUT -> "PUT"
            HttpMethod.PATCH -> "PATCH"
            HttpMethod.DELETE -> "DELETE"
        }
    }
}