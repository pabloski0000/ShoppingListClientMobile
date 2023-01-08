package main.shoppilientmobile.registrationFeature.apis.mocks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.core.remote.StreamingHttpClient

class StreamingHttpClientMock: StreamingHttpClient {
    lateinit var lastRequest: HttpRequest

    override suspend fun makeRequest(httpRequest: HttpRequest): Flow<String> {
        lastRequest = httpRequest
        return flow { "" }
    }
}