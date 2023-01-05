package main.shoppilientmobile.application

import main.shoppilientmobile.createListFeature.Product
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList

class AddProductUseCase(
    private val sharedShoppingList: SharedShoppingList
) {
    fun addProduct(product: Product) {
        sharedShoppingList.addProduct(product)
    }
}