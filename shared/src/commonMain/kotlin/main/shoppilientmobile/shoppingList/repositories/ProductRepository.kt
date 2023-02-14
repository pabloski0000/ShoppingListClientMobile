package main.shoppilientmobile.shoppingList.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.dataSources.ProductRemoteDataSource

class ProductRepository(
    private val productRemoteDataSource: ProductRemoteDataSource,
) {
    suspend fun getProducts(): List<Product> = productRemoteDataSource.getProducts()
}