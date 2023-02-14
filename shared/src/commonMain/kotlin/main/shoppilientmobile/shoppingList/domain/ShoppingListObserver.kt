package main.shoppilientmobile.shoppingList.domain

import main.shoppilientmobile.domain.Product

interface ShoppingListObserver : ShoppingListDeletionsObserver {
    fun stateAtTheMomentOfSubscribing(currentList: List<Product>)
    fun productAdded(product: Product)
    fun productModified(oldProduct: Product, newProduct: Product)
    fun shoppingListRecreated(currentList: List<Product>)
}