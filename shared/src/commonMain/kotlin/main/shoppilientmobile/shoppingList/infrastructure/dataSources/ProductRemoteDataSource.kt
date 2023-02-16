package main.shoppilientmobile.shoppingList.infrastructure.dataSources

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ProductApi

class ProductRemoteDataSource(
    private val productApi: ProductApi,
) {
    suspend fun getProducts(): List<Product> {
        return productApi.getProducts()
    }
}