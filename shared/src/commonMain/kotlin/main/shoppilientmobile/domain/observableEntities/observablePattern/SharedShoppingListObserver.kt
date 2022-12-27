package main.shoppilientmobile.domain.observableEntities.observablePattern

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.User

interface SharedShoppingListObserver {
    fun productAdded(product: Product)
    fun productModified(oldProduct: Product, newProduct: Product)
    fun productRemoved(product: Product)
    fun userRegistered(user: User)
}