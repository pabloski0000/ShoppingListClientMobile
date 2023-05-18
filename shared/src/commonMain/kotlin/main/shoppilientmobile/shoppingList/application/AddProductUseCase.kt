package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

class AddProductUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) {
    suspend fun addProduct(product: Product, exceptionListener: RequestExceptionListener) {
        remoteShoppingList.addProduct(product, exceptionListener)
    }
}