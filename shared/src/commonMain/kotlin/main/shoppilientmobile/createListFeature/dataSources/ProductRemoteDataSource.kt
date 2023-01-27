package main.shoppilientmobile.createListFeature.dataSources

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.createListFeature.dataSources.apis.ProductApi

class ProductRemoteDataSource(
    private val productApi: ProductApi,
) {
    suspend fun getProducts(): List<Product> {
        return productApi.getProducts()
    }
}