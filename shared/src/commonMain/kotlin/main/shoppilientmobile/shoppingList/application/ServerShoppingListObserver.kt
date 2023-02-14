package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

interface ServerShoppingListObserver {
    fun stateAtTheMomentOfSubscribing(products: List<Product>)
    fun productAdded(product: Product)
    fun productModified(oldProduct: Product, newProduct: Product)
}