package main.shoppilientmobile.shoppingList.application.doubles

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.ProductRepository

class ProductRepositoryFake : ProductRepository {
    val products = mutableListOf<Product>()

    override fun add(product: Product) {
        products.add(product)
    }

    override fun exists(product: Product): Boolean {
        return products.contains(product)
    }
}