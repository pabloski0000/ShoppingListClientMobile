package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.Product

data class ProductItemState(
    val productDescription: String,
    val markedToBeDeleted: Boolean,
) {
    fun toProduct(): Product {
        return Product(productDescription)
    }
}