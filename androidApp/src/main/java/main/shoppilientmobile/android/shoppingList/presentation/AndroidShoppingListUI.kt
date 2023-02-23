package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.shoppingList.application.AddProductUseCase
import main.shoppilientmobile.shoppingList.application.ModifyProductUseCase
import main.shoppilientmobile.shoppingList.application.SynchroniseWithRemoteShoppingListUseCase

class AndroidShoppingListUI(
    private val addProductUseCase: AddProductUseCase,
    private val modifyProductUseCase: ModifyProductUseCase,
    private val synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase,
) : main.shoppilientmobile.shoppingList.application.ShoppingListObserver {
    private var observers = emptySet<ShoppingListObserver>()
    private var shoppingList = emptyList<Product>()
    private var observingShoppingList = false


    fun addProducts(products: List<Product>) {
        products.map { addProductUseCase.addProduct(it.toProduct()) }
    }

    fun addProduct(product: Product) {
        addProductUseCase.addProduct(
            product.toProduct()
        )
    }

    fun modifyProduct(oldProduct: Product, newProduct: Product) {
        modifyProductUseCase.modifyProduct(oldProduct.toProduct(), newProduct.toProduct())
    }

    fun observeShoppingList(observer: ShoppingListObserver) {
        if (! observingShoppingList) {
            synchroniseWithRemoteShoppingListUseCase.synchronise(observer = this)
            observingShoppingList = true
        }
        observer.currentState(shoppingList)
        observers = setOf(*observers.toTypedArray(), observer)
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
        val productToModify = Product.fromProduct(oldProduct)
        val modifiedProduct = Product.fromProduct(newProduct)
        shoppingList = shoppingList.map { product ->
            if (product == productToModify) {
                return@map modifiedProduct
            }
            product
        }
        observers.map { it.productModified(productToModify, modifiedProduct) }
    }

    override fun productDeleted(product: main.shoppilientmobile.domain.Product) {
        shoppingList = shoppingList.filter { deletedProduct ->
            if (deletedProduct.toProduct() == product) {
                observers.map { observer -> observer.productDeleted(deletedProduct) }
                false
            }
            true
        }
    }
}