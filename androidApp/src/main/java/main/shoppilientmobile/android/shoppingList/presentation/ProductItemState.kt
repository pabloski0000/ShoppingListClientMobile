package main.shoppilientmobile.android.shoppingList.presentation

data class ProductItemState(
    val content: String,
    val selected: Boolean,
) {
    companion object {
        fun fromProduct(product: main.shoppilientmobile.domain.Product): ProductItemState {
            return ProductItemState(product.description, false)
        }

        fun fromProduct2(product: Product): ProductItemState {
            return ProductItemState(product.description, false)
        }
    }
    fun toProduct(): main.shoppilientmobile.domain.Product {
        return main.shoppilientmobile.domain.Product(content)
    }

    fun toProduct2(): Product {
        return Product(content)
    }
}