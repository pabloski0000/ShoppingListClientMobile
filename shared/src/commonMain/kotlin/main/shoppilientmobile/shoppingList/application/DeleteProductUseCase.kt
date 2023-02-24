package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

class DeleteProductUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) {
    fun deleteProduct(product: Product) {
        remoteShoppingList.deleteProduct(product)
    }
}