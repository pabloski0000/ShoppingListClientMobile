package main.shoppilientmobile.createListFeature

import main.shoppilientmobile.createListFeature.rules.ExceededMaximumProductDescriptionLengthException
import main.shoppilientmobile.createListFeature.rules.ProductRules
import main.shoppilientmobile.createListFeature.rules.UnreachedMinimumProductDescriptionLengthException
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
                    assertTrue { e is UnreachedMinimumProductDescriptionLengthException }
                else if (i > productDescriptionMaximumIncludedLength)
                    assertTrue { e is ExceededMaximumProductDescriptionLengthException }
                else
                    assertTrue("No exception should have been thrown because" +
                            " product description was between the lengths limits") { false }
            }
            randomProductDescription.append("a")
        }
    }


}