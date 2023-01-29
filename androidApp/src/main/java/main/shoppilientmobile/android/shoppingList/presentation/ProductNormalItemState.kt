package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.Product

data class ProductNormalItemState(val content: String) {
    companion object {
        fun fromProduct(product: Product): ProductNormalItemState {
            return ProductNormalItemState(product.description)
        }
    }

    fun toProduct(): Product {
        return Product(content)
    }
}