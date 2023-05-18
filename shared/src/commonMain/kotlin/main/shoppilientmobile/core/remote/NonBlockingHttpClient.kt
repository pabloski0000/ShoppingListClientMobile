package main.shoppilientmobile.core.remote

interface NonBlockingHttpClient {
    suspend fun makeRequest(httpRequest: HttpRequest): HttpResponse
}