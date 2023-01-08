package main.shoppilientmobile.core.remote

import kotlinx.coroutines.flow.Flow

interface StreamingHttpClient {
    suspend fun makeRequest(httpRequest: HttpRequest): Flow<String>
}