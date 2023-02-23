package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

class AddProductUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) {
    fun addProduct(product: Product) {
        remoteShoppingList.addProduct(product)
    }
}