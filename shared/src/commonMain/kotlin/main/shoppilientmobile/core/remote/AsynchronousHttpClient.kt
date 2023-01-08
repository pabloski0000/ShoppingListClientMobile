package main.shoppilientmobile.core.remote

interface AsynchronousHttpClient {
    suspend fun makeRequest(httpRequest: HttpRequest): HttpResponse
}