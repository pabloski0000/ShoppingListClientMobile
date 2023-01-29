package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.Product

data class DeletableProductItemState(val content: String, val selected: Boolean) {
    companion object {
        fun fromProduct(product: Product): DeletableProductItemState {
            return DeletableProductItemState(product.description, false)
        }
    }
    fun toProduct(): Product {
        return Product(content)
    }
}