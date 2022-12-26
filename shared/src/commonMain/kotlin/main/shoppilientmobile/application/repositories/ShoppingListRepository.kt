package main.shoppilientmobile.application.repositories

import main.shoppilientmobile.domain.Product

interface ShoppingListRepository {
    fun getProducts(): List<Product>
    fun addProduct(product: Product)
    fun modifyProduct(oldProduct: Product, newProduct: Product)
    fun removeProduct(product: Product)
}