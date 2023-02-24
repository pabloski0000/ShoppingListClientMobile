package main.shoppilientmobile.android.shoppingList.presentation.testDoubles

import kotlinx.coroutines.*
import main.shoppilientmobile.android.shoppingList.presentation.Product
import main.shoppilientmobile.android.shoppingList.presentation.ShoppingListObserver

class LocalShoppingListSpy : ShoppingListObserver {
    private var shoppingList = emptyList<Product>()
    private val coroutineScopeOnMainThread = CoroutineScope(Dispatchers.Main)

    override fun currentState(products: List<Product>) {
        shoppingList = products
    }

    override fun productAdded(product: Product) {
        shoppingList = listOf(*shoppingList.toTypedArray(), product)
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        shoppingList = shoppingList.map { product ->
            if (product == oldProduct) {
                return@map newProduct
            }
            return@map product
        }
    }

    override fun productDeleted(product: Product) {
        shoppingList = shoppingList.filter { it != product }
    }

    @Throws(Exception::class)
    fun assertShoppingListStateIsExactlyThisOrThrowException(products: List<Product>) {
        try {
            runBlocking {
                withTimeout(10_000) {
                    while (true) {
                        if (shoppingList.size == products.size && shoppingList.toSet() == products.toSet()) {
                            return@withTimeout
                        }
                        delay(500)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            throw Exception("Inconsistent external shopping list state in test: State should be" +
                    " $products but is $shoppingList")
        }
    }
}