package main.shoppilientmobile.core.remote

data class HttpResponse(
    val statusCode: Int,
    val headers: Map<String, List<String>>,
    val body: String,
)
