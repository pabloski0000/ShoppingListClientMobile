package main.shoppilientmobile.createListFeature.repositories

import main.shoppilientmobile.createListFeature.Product
import main.shoppilientmobile.createListFeature.dataSources.ProductRemoteDataSource

class ProductRepository(
    private val productRemoteDataSource: ProductRemoteDataSource,
) {
    suspend fun getProducts(): List<Product> = productRemoteDataSource.getProducts()
}