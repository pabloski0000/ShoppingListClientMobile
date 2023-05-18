package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

interface RemoteShoppingList {
    suspend fun addProduct(product: Product, exceptionListener: RequestExceptionListener)
    suspend fun modifyProduct(oldProduct: Product, newProduct: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun deleteAllProducts()
    fun subscribe(observer: SharedShoppingListObserver)
    fun listen()
    suspend fun stopListening()
}