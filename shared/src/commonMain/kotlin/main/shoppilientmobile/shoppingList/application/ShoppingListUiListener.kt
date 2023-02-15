package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

class ShoppingListUiListener(
    shoppingListUI: ShoppingListUI,
    private val remoteShoppingList: RemoteShoppingList,
) {
    init {
        shoppingListUI.addShoppingListUIListener(this)
    }

    fun addProduct(product: Product) {
        remoteShoppingList.addProduct(product)
    }

    fun modifyProduct(oldProduct: Product, newProduct: Product) {
        remoteShoppingList.modifyProduct(oldProduct, newProduct)
    }

    fun removeProduct(product: Product) {
        remoteShoppingList.deleteProduct(product)
    }

    fun observeShoppingList(observer: ShoppingListObserver) {
        remoteShoppingList.observe(observer)
    }
}