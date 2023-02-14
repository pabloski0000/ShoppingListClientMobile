package main.shoppilientmobile.shoppingList.domain

import main.shoppilientmobile.domain.Product

interface ShoppingListDeletionsObserver {
    fun productDeleted(product: Product)
}