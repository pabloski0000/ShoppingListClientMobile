package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.ExternalSharedShoppingList
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.User

class ExternalSharedShoppingListSynchronizer(
    private val externalSharedShoppingList: ExternalSharedShoppingList,
) {

    fun productAdded(product: Product) {
        externalSharedShoppingList.addProduct(product)
    }

    fun productModified(oldProduct: Product, newProduct: Product) {
        externalSharedShoppingList.modifyProduct(oldProduct, newProduct)
    }

    fun productRemoved(product: Product) {
        externalSharedShoppingList.removeProduct(product)
    }

    fun userRegistered(user: User) {
        externalSharedShoppingList.registerUser(user)
    }

}