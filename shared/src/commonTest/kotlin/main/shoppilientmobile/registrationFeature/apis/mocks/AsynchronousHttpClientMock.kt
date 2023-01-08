package main.shoppilientmobile.registrationFeature.apis.mocks

import main.shoppilientmobile.core.remote.AsynchronousHttpClient
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.core.remote.HttpResponse

class AsynchronousHttpClientMock: AsynchronousHttpClient {
    lateinit var lastRequest: HttpRequest

    override suspend fun makeRequest(httpRequest: HttpRequest): HttpResponse {
        lastRequest = httpRequest
        return HttpResponse(
            statusCode = 200,
            headers = mapOf("" to listOf("")),
            body = """{"accessToken": "lsadjflkasjflsa.sajdlfkajs.ajsdfklajsld"}""",
        )
    }

}