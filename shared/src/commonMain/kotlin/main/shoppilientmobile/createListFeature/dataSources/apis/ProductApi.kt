package main.shoppilientmobile.createListFeature.dataSources.apis

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import main.shoppilientmobile.createListFeature.Product
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.jsonStructures.JsonStructure

class ProductApi(
    private val httpClient: HttpClient,
) {
    suspend fun getProducts(): List<Product> {
        val url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products"
        val response = httpClient.get {
            url(url)
            headers {
                append("Content-Type", "application/json")
                append("Accept", "application/json")
            }
        }.body<List<JsonStructure.Product>>()
        return response.map { Product(it.name) }
    }
}