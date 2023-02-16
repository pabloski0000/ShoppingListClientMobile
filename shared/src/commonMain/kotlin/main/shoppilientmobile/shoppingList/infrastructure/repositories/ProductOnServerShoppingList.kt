package main.shoppilientmobile.shoppingList.infrastructure.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ProductBuilder

class ProductOnServerShoppingList(val id: String, val description: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProductOnServerShoppingList) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun toProduct(): Product {
        return ProductBuilder().assignDescription(description).build()
    }
}