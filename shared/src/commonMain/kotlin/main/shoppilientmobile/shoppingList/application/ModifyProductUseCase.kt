package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

class ModifyProductUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) {
    suspend fun modifyProduct(oldProduct: Product, newProduct: Product) {
        remoteShoppingList.modifyProduct(oldProduct, newProduct)
    }

    fun modifyProduct(oldProduct: Product, newProduct: Product, response: Response) {
        remoteShoppingList.modifyProduct(oldProduct, newProduct, response)
    }
}