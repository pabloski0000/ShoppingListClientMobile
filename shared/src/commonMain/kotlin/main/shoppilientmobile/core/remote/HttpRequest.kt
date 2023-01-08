package main.shoppilientmobile.core.remote

data class HttpRequest(
    val httpMethod: HttpMethod,
    val url: String,
    val headers: Map<String, String>,
    val body: String,
)
