package main.shoppilientmobile.createListFeature.dataSources

import io.ktor.client.*
import main.shoppilientmobile.createListFeature.Product
import main.shoppilientmobile.createListFeature.dataSources.apis.ProductApi

class ProductRemoteDataSource(
    private val productApi: ProductApi,
) {
    suspend fun getProducts(): List<Product> {
        return productApi.getProducts()
    }
}