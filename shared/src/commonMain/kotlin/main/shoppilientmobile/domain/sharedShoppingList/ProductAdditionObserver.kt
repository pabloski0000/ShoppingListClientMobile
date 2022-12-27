package main.shoppilientmobile.domain.sharedShoppingList

import main.shoppilientmobile.domain.Product

interface ProductAdditionObserver {
    fun productAdded(product: Product)
}