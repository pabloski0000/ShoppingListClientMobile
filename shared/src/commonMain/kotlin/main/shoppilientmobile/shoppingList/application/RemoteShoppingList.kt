package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

interface RemoteShoppingList {
    fun addProduct(product: Product)
    fun modifyProduct(oldProduct: Product, newProduct: Product)
    fun deleteProduct(product: Product)
    fun observe(observer: ShoppingListObserver)
}