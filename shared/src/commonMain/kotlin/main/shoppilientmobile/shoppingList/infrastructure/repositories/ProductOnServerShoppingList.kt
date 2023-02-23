package main.shoppilientmobile.shoppingList.infrastructure.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ProductBuilder

class ProductOnServerShoppingList(val id: String, val description: String) {

    companion object {
        fun fromProduct(product: Product): ProductOnServerShoppingList {
            return ProductOnServerShoppingList(
                generateRandomId(),
                product.description,
            )
        }

        fun generateRandomId(): String {
            val setOfPossibleCharacters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            val randomId = buildString {
                for (i in 0 until 20) {
                    append(setOfPossibleCharacters.random())
                }
            }
            return randomId
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProductOnServerShoppingList) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun copy(id: String = this.id, description: String = this.description): ProductOnServerShoppingList {
        return ProductOnServerShoppingList(id, description)
    }

    fun toProduct(): Product {
        return ProductBuilder().assignDescription(description).build()
    }
}