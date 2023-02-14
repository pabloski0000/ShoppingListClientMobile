package main.shoppilientmobile.domain

import main.shoppilientmobile.shoppingList.rules.ExceededMaximumProductDescriptionLengthException
import main.shoppilientmobile.shoppingList.rules.ProductRules
import main.shoppilientmobile.shoppingList.rules.UnreachedMinimumProductDescriptionLengthException

data class Product(val description: String) {
    init {
        if (description.length < ProductRules.minimumProductDescriptionLength) {
            throw UnreachedMinimumProductDescriptionLengthException("A product has to at least" +
                    "has a length of ${ProductRules.minimumProductDescriptionLength} characters")
        }
        if (description.length > ProductRules.maximumProductDescriptionLength) {
            throw ExceededMaximumProductDescriptionLengthException("A product cannot exceed" +
                    "the maximum length of ${ProductRules.maximumProductDescriptionLength} characters")
        }
    }

    override fun toString(): String {
        return description
    }
}