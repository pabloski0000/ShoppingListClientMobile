package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.ExternalSharedShoppingList
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.observableEntities.observablePattern.RegisterToObservableSharedShoppingListUseCase
import main.shoppilientmobile.domain.observableEntities.observablePattern.SharedShoppingListObserver

class ExternalSharedShoppingListSynchronizer(
    registerObserverToSharedShoppingListUseCase: RegisterToObservableSharedShoppingListUseCase,
    private val externalSharedShoppingList: ExternalSharedShoppingList,
): SharedShoppingListObserver {
    init {
        registerObserverToSharedShoppingListUseCase.registerObserver(this)
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