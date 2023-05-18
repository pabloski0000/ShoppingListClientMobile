package main.shoppilientmobile.android.shoppingList.presentation

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.shoppingList.application.*
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.GetLocalUserUseCase

class AndroidShoppingListUI(
    private val addProductUseCase: AddProductUseCase,
    private val modifyProductUseCase: ModifyProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase,
    private val getLocalUserUseCase: GetLocalUserUseCase,
    private val listenToUserRegistrationsUseCase: ListenToUserRegistrationsUseCase,
) : main.shoppilientmobile.shoppingList.application.SharedShoppingListObserver {
    private var observers = emptySet<ShoppingListObserver>()
    private var shoppingList = emptyList<Product>()
    private var observingShoppingList = false


    suspend fun addProducts(products: List<Product>, exceptionListener: RequestExceptionListener) {
        products.map { addProductUseCase.addProduct(it.toProduct(), exceptionListener) }
    }

    suspend fun addProduct(product: Product, exceptionListener: RequestExceptionListener) {
        addProductUseCase.addProduct(
            product.toProduct(),
            exceptionListener,
        )
    }

    suspend fun modifyProduct(oldProduct: Product, newProduct: Product) {
        modifyProductUseCase.modifyProduct(oldProduct.toProduct(), newProduct.toProduct())
    }

    suspend fun deleteProduct(product: Product) {
        deleteProductUseCase.deleteProduct(product.toProduct())
    }

    fun observeShoppingList(observer: ShoppingListObserver) {
        if (! observingShoppingList) {
            synchroniseWithRemoteShoppingListUseCase.synchronise(observer = this)
            observingShoppingList = true
        }
        observer.currentState(shoppingList)
        observers = setOf(*observers.toTypedArray(), observer)
    }

    suspend fun userIsAdmin(): Boolean {
        val user = getLocalUserUseCase.getLocalUser()
        return user is User && user.getRole() == UserRole.ADMIN
    }

    suspend fun listenToUserRegistrations(userRegistrationsListener: UserRegistrationsListener) {
        listenToUserRegistrationsUseCase.listen(userRegistrationsListener)
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
            } else {
                true
            }
        }
        println()
    }
}