package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.Product

data class ProductItemState(
    val content: String,
    val selected: Boolean,
) {
    fun toProduct(): Product {
        return Product(content)
    }
}