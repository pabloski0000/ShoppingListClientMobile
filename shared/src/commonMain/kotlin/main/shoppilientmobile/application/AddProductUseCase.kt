package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.repositories.ShoppingListRepository
import main.shoppilientmobile.application.updateCommand.UpdateCommandImpl
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ShoppingList

class AddProductUseCase(
    private val shoppingList: ShoppingList,
    private val shoppingListRepository: ShoppingListRepository,
) {
    fun addProduct(product: Product) {
        UpdateCommandImpl(
            domainUpdate = { shoppingList.addProduct(product) },
            repositoryUpdate = { shoppingListRepository.addProduct(product) },
            domainUpdateRollback = { shoppingList.removeProduct(product) },
            repositoryUpdateRollback = { shoppingListRepository.removeProduct(product) },
        ).execute()
    }
}