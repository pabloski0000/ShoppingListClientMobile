package main.shoppilientmobile.shoppingList.infrastructure.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.ProductRemoteDataSource

class ProductRepository(
    private val productRemoteDataSource: ProductRemoteDataSource,
) {
    suspend fun getProducts(): List<Product> = productRemoteDataSource.getProducts()
}