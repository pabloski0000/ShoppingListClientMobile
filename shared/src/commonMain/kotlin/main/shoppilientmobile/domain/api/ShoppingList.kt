package main.shoppilientmobile.domain.api

import main.shoppilientmobile.domain.Product

interface ShoppingList {
    fun getProducts(): List<Product>
    fun addProduct(product: Product)
    fun modifyProduct(oldProduct: Product, newProduct: Product)
    fun removeProduct(product: Product)
    fun contains(product: Product): Boolean
}