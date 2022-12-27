package main.shoppilientmobile.application

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.observableEntities.observablePattern.SharedShoppingListObservable
import main.shoppilientmobile.domain.observableEntities.observablePattern.SharedShoppingListObserver

class ExternalSharedShoppingListSynchronizer(
    private val externalSharedShoppingList: SharedShoppingList,
    sharedShoppingListObservable: SharedShoppingListObservable,
): SharedShoppingListObserver {
    init {
        sharedShoppingListObservable.registerObserver(this)
    }

    override fun productAdded(product: Product) {
        externalSharedShoppingList.addProduct(product)
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        externalSharedShoppingList.modifyProduct(oldProduct, newProduct)
    }

    override fun productRemoved(product: Product) {
        externalSharedShoppingList.removeProduct(product)
    }

    override fun userRegistered(user: User) {
        externalSharedShoppingList.registerUser(user)
    }

}