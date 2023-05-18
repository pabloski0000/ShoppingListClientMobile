package main.shoppilientmobile.core.remote

import kotlinx.coroutines.flow.Flow

interface StreamingHttpClient {
    suspend fun makeStreamingRequest(httpRequest: HttpRequest): Flow<String>
    suspend fun stopStreamingRequest()
}