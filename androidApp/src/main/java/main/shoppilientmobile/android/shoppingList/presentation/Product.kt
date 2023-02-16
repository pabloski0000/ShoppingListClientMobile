package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.domainExposure.ProductBuilder

data class Product(val description: String) {
    companion object {
        fun fromProduct(product: main.shoppilientmobile.domain.Product): Product {
            return Product(product.description)
        }
    }

    fun toProduct(): main.shoppilientmobile.domain.Product {
        return ProductBuilder().assignDescription(description).build()
    }
}
