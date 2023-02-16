package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

interface ShoppingListObserver {
    fun currentState(products: List<Product>)
    fun productAdded(product: Product)
    fun productModified(oldProduct: Product, newProduct: Product)
    fun productDeleted(product: Product)
}