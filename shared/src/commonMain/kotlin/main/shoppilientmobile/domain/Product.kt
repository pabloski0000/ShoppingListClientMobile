package main.shoppilientmobile.domain

import main.shoppilientmobile.domain.exceptions.ProductDescriptionExceedsMaximumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionIsShorterThanMinimumLengthException
import main.shoppilientmobile.shoppingList.domain.rules.ProductRules

data class Product(val description: String) {
    init {
        if (description.length < ProductRules.minimumProductDescriptionLength) {
            throw ProductDescriptionIsShorterThanMinimumLengthException("A product has to at least" +
                    "has a length of ${ProductRules.minimumProductDescriptionLength} characters")
        }
        if (description.length > ProductRules.maximumProductDescriptionLength) {
            throw ProductDescriptionExceedsMaximumLengthException("A product cannot exceed" +
                    "the maximum length of ${ProductRules.maximumProductDescriptionLength} characters")
        }
    }



    override fun toString(): String {
        return description
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        if (description.lowercase() != other.description.lowercase()) return false

        return true
    }

    override fun hashCode(): Int {
        return description.lowercase().hashCode()
    }
}