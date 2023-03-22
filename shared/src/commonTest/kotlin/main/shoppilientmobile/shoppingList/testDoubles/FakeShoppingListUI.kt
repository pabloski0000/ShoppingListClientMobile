package main.shoppilientmobile.shoppingList.testDoubles

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.ShoppingListObserver
import main.shoppilientmobile.shoppingList.application.ShoppingListUI
import main.shoppilientmobile.shoppingList.application.ShoppingListUiListener

class FakeShoppingListUI : ShoppingListUI, ShoppingListObserver {
    private var shoppingListUiListener: ShoppingListUiListener? = null
    private var fakeProductList = emptyList<Product>()

    override fun addShoppingListUIListener(uiListener: ShoppingListUiListener) {
        shoppingListUiListener = uiListener
    }

    fun observerShoppingList() {
        shoppingListUiListener?.observeShoppingList(this)
    }

    override fun currentState(products: List<Product>) {
        fakeProductList = products
    }

    override fun productAdded(product: Product) {
        fakeProductList = listOf(*fakeProductList.toTypedArray(), product)
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        fakeProductList = fakeProductList.map { product ->
            if (product == oldProduct) {
                return@map newProduct
            }
            return@map product
        }
    }

    override fun productDeleted(product: Product) {
        fakeProductList = fakeProductList.filter { it != product }
    }

    suspend fun addProducts(products: List<Product>) {
        products.map { product ->
            shoppingListUiListener?.addProduct(product)
        }
    }

    suspend fun modifyProduct(oldProduct: Product, newProduct: Product) {
        shoppingListUiListener?.modifyProduct(oldProduct, newProduct)
    }

    suspend fun removeProduct(product: Product) {
        shoppingListUiListener?.removeProduct(product)
    }

    @Throws(Exception::class)
    fun assertProductsExistOrThrowException(products: List<Product>) {
        if (! fakeProductList.containsAll(products)) {
            throw Exception("Product should exist in the ui")
        }
    }

    @Throws(Exception::class)
    fun assertProductsDoNotExistOrThrowException(products: List<Product>) {
        if (fakeProductList.containsAll(products)) {
            throw Exception("Product should not exist in the ui")
        }
    }
}