package main.shoppilientmobile.android.userRegistrationFeature.infrastructure.doubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.core.remote.StreamingHttpClient

class StreamingHttpClientSpy : StreamingHttpClient {
    var lastRequest: HttpRequest? = null

    override suspend fun makeRequest(httpRequest: HttpRequest): Flow<String> {
        lastRequest = httpRequest
        return flowOf()
    }
}