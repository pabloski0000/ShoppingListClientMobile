package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.domainExposure.ProductBuilder
import main.shoppilientmobile.shoppingList.application.ShoppingListUI
import main.shoppilientmobile.shoppingList.application.ShoppingListUiListener

class AndroidShoppingListUI : ShoppingListUI, main.shoppilientmobile.shoppingList.application.ShoppingListObserver {
    private var shoppingListUiListener: ShoppingListUiListener? = null
    private var observers = emptyList<ShoppingListObserver>()
    private var shoppingList = emptyList<Product>()
    private var observingShoppingList = false

    override fun addShoppingListUIListener(uiListener: ShoppingListUiListener) {
        shoppingListUiListener = uiListener
    }

    fun addProduct(product: Product) {
        shoppingListUiListener?.addProduct(
            product.toProduct()
        )
    }

    fun observeShoppingList(observer: ShoppingListObserver) {
        if (! observingShoppingList) {
            shoppingListUiListener?.observeShoppingList(this)
            observingShoppingList = true
        }
        observer.currentState(shoppingList)
        observers = listOf(*observers.toTypedArray(), observer)
    }

    override fun currentState(products: List<main.shoppilientmobile.domain.Product>) {
        shoppingList = products.map { Product.fromProduct(it) }
        observers.map { it.currentState(shoppingList) }
    }

    override fun productAdded(product: main.shoppilientmobile.domain.Product) {
        val addedProduct = Product.fromProduct(product)
        shoppingList = listOf(*shoppingList.toTypedArray(), addedProduct)
        observers.map { it.productAdded(addedProduct) }
    }

    override fun productModified(
        oldProduct: main.shoppilientmobile.domain.Product,
        newProduct: main.shoppilientmobile.domain.Product,
    ) {
        TODO("Not yet implemented")
    }

    override fun productDeleted(product: main.shoppilientmobile.domain.Product) {
        TODO("Not yet implemented")
    }
}