package main.shoppilientmobile.domain.sharedShoppingList

import main.shoppilientmobile.domain.Product

interface ProductModificationObserver {
    fun productModified(oldProduct: Product, newProduct: Product)
}