package main.shoppilientmobile.android.shoppingList.presentation.testUtils

object ProductItemCreator {
    fun createRandomProducts(amountOfProducts: Int): List<String> {
        val products = mutableListOf<String>()
        for (i in 1..amountOfProducts) {
            products.add(
                createRandomProduct()
            )
        }
        return products
    }

    fun createRandomProduct(): String {
        val productLength = 5..11
        val allowedCharacters = ('a'..'z') + ('A'..'Z')
        val product = buildString() {
            for (i in 0 until productLength.random()) {
                append(allowedCharacters.random().toString())
            }
        }
        return product
    }
}
