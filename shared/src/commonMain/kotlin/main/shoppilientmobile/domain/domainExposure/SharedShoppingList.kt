package main.shoppilientmobile.domain.domainExposure

import main.shoppilientmobile.createListFeature.Product

interface SharedShoppingList {
    fun getProducts(): List<Product>
    fun addProduct(product: Product)
    fun modifyProduct(oldProduct: Product, newProduct: Product)
    fun removeProduct(product: Product)
    fun contains(product: Product): Boolean
    fun registerUser(user: User)
    fun getRegisteredUsers(): List<User>
}