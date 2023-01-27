package main.shoppilientmobile.createListFeature.dataSources.apis

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.core.remote.AsynchronousHttpClient
import main.shoppilientmobile.core.remote.HttpMethod
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.domain.Product

class ProductApi(
    private val httpClient: AsynchronousHttpClient,
) {
    suspend fun getProducts(): List<Product> {
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.GET,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products",
            headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
            ),
            body = "",
        )
        val response = httpClient.makeRequest(httpRequest)
        val json = Json.parseToJsonElement(response.body).jsonArray
        return json.map { jsonElement ->
            Product(
                description = jsonElement.jsonObject.getValue("name").jsonPrimitive.content
            )
        }
    }
}