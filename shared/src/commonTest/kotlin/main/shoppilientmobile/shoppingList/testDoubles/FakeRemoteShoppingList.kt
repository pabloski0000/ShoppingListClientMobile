package main.shoppilientmobile.shoppingList.testDoubles

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.shoppingList.application.ShoppingListObserver

class FakeRemoteShoppingList : RemoteShoppingList {
    private var observers = emptyList<ShoppingListObserver>()
    private var fakeProductList = emptyList<Product>()

    override suspend fun addProduct(product: Product) {
        fakeProductList = listOf(*fakeProductList.toTypedArray(), product)
        observers.map { it.productAdded(product) }
    }

    override suspend fun modifyProduct(oldProduct: Product, newProduct: Product) {
        fakeProductList = fakeProductList.map { product ->
            if (product == oldProduct) {
                return@map newProduct
            }
            return@map product
        }
        observers.map { it.productModified(oldProduct, newProduct) }
    }

    override suspend fun deleteProduct(product: Product) {
        fakeProductList = fakeProductList.filter { it != product }
        observers.map { it.productDeleted(product) }
    }

    override fun deleteAllProducts() {
        TODO("Not yet implemented")
    }

    override fun observe(observer: ShoppingListObserver) {
        observers = listOf(*observers.toTypedArray(), observer)
    }

    @Throws(Exception::class)
    fun assertProductsDoNotExistOrThrowException(products: List<Product>) {
        if (fakeProductList.containsAll(products)) {
            throw Exception("Products should not exist in remote shopping list")
        }
    }

    @Throws(Exception::class)
    fun assertProductsExistOrThrowException(products: List<Product>) {
        if (! fakeProductList.containsAll(products)) {
            throw Exception("Products should exist in remote shopping list")
        }
    }
}