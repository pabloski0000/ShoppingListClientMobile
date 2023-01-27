package main.shoppilientmobile.android.shoppingList.domain

import main.shoppilientmobile.domain.Product

interface ShoppingListObserver {
    fun stateAtTheMomentOfSubscribing(currentList: List<Product>)
    fun productAdded(product: Product)
    fun productModified(oldProduct: Product, newProduct: Product)
    fun productDeleted(product: Product)
}