package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

interface RemoteShoppingList {
    suspend fun addProduct(product: Product)
    suspend fun modifyProduct(oldProduct: Product, newProduct: Product)
    suspend fun deleteProduct(product: Product)
    fun deleteAllProducts()
    fun observe(observer: ShoppingListObserver)
}