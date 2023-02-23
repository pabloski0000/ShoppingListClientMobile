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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        return toProduct() == other.toProduct()
    }

    override fun hashCode(): Int {
        return toProduct().hashCode()
    }
}
