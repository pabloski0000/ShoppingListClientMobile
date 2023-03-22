package main.shoppilientmobile.shoppingList

import main.shoppilientmobile.shoppingList.domain.rules.ProductRules
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.exceptions.ProductDescriptionExceedsMaximumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionIsShorterThanMinimumLengthException
import kotlin.test.Test
import kotlin.test.assertTrue

class ProductRestrictions {
    private val productDescriptionMinimumIncludedLength = ProductRules.minimumProductDescriptionLength
    private val productDescriptionMaximumIncludedLength = ProductRules.maximumProductDescriptionLength

    @Test
    fun productDescriptionNeedsToConstrainToAMaximumAndMinimumLength() {
        val randomProductDescription = StringBuilder("")
        for (i in 0..productDescriptionMaximumIncludedLength + 1) {
            try {
                Product(randomProductDescription.toString())
                if (i !in productDescriptionMinimumIncludedLength..productDescriptionMaximumIncludedLength)
                    assertTrue("Product cannot be created when its description" +
                            " length is not in the predefined range") { false }
            } catch (e: Exception) {
                if (i < productDescriptionMinimumIncludedLength)
                    assertTrue { e is ProductDescriptionIsShorterThanMinimumLengthException }
                else if (i > productDescriptionMaximumIncludedLength)
                    assertTrue { e is ProductDescriptionExceedsMaximumLengthException }
                else
                    assertTrue("No exception should have been thrown because" +
                            " product description was between the lengths limits") { false }
            }
            randomProductDescription.append("a")
        }
    }


}