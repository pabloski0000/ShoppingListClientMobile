package main.shoppilientmobile.android.shoppingList.presentation.testDoubles

import kotlinx.coroutines.*
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.ShoppingListObserver

class ExternalShoppingListSpy : ShoppingListObserver {
    private var state = emptyList<Product>()

    override fun currentState(products: List<Product>) {
        state = products
    }

    override fun productAdded(product: Product) {
        state = listOf(*state.toTypedArray(), product)
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        state = state.map { product ->
            if (product == oldProduct) {
                return@map newProduct
            }
            return@map product
        }
    }

    override fun productDeleted(product: Product) {
        state = state.filter { it != product }
    }

    @Throws(Exception::class)
    fun assertShoppingListStateIsExactlyThisOrThrowException(products: List<Product>) {
        try {
            runBlocking {
                withTimeout(10_000) {
                    while (true) {
                        if (state.size == products.size && state.toSet() == products.toSet()) {
                            return@withTimeout
                        }
                        delay(500)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            throw Exception("Inconsistent external shopping list state in test")
        }
    }
}