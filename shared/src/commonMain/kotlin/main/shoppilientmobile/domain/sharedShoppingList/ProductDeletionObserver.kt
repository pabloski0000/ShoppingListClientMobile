package main.shoppilientmobile.domain.sharedShoppingList

import main.shoppilientmobile.domain.Product

interface ProductDeletionObserver {
    fun productRemoved(product: Product)
}