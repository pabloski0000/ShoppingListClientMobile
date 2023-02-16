package main.shoppilientmobile.shoppingList.infrastructure

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ProductOnServerShoppingList

interface ServerShoppingListObserver {
    fun currentState(product: ProductOnServerShoppingList)
    fun productAdded(product: ProductOnServerShoppingList)
    fun productModified(modifiedProduct: ProductOnServerShoppingList)
    fun productDeleted(productId: String)
}