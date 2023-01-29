package main.shoppilientmobile.android.shoppingList.domain

import main.shoppilientmobile.domain.Product

interface ShoppingListDeletionsObserver {
    fun productDeleted(product: Product)
}