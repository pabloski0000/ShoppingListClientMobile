package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.Product

data class ProductItemState(
    val content: String,
    val selected: Boolean,
) {
    companion object {
        fun fromProduct(product: Product): ProductItemState {
            return ProductItemState(product.description, false)
        }
    }
    fun toProduct(): Product {
        return Product(content)
    }
}