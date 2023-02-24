package main.shoppilientmobile.shoppingList.infrastructure.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.shoppingList.application.ShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.ServerShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi2

class ServerShoppingList(
    private val serverShoppingListApi: ServerShoppingListApi2,
) : RemoteShoppingList, ServerShoppingListObserver {
    private var observers = setOf<ShoppingListObserver>()
    private var state = emptyList<ProductOnServerShoppingList>()
    private var observingRemoteShoppingList = false

    override fun addProduct(product: Product) {
        serverShoppingListApi.addProduct(ProductOnServerShoppingList.fromProduct(product))
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        val productToModify = state.find { it.toProduct() == oldProduct }!!
        val modifiedProduct = productToModify.copy(description = newProduct.description)
        serverShoppingListApi.modifyProduct(modifiedProduct)
    }

    override fun deleteProduct(product: Product) {
        val productToDelete = state.find { it.toProduct() == product }!!
        serverShoppingListApi.deleteProduct(productToDelete)
    }

    override fun deleteAllProducts() {
        serverShoppingListApi.deleteAllProducts()
    }

    override fun observe(observer: ShoppingListObserver) {
        observers = setOf(*observers.toTypedArray(), observer)
        if (! observingRemoteShoppingList) {
            serverShoppingListApi.observeServerShoppingList(this)
            observingRemoteShoppingList = true
        }
        observer.currentState(
            state.map { productOnServer -> productOnServer.toProduct() }
        )
    }

    override fun stateAtTheMomentOfSubscribing(product: ProductOnServerShoppingList) {
        state = listOf(
            *state.toTypedArray(),
            product,
        )
        observers.map { observer ->
            observer.currentState(
                state.map { productOnServer -> productOnServer.toProduct() }
            )
        }
    }

    override fun productAdded(product: ProductOnServerShoppingList) {
        state = listOf(
            *state.toTypedArray(),
            product,
        )
        observers.map { observer ->
            observer.productAdded(product.toProduct())
        }
    }

    override fun productModified(modifiedProduct: ProductOnServerShoppingList) {
        val oldProduct = state.find { productOnServer ->
            productOnServer == modifiedProduct
        }!!
        state = state.map { productOnServer ->
            if (productOnServer == modifiedProduct) {
                return@map modifiedProduct
            }
            return@map productOnServer
        }
        observers.map { observer ->
            observer.productModified(oldProduct.toProduct(), modifiedProduct.toProduct())
        }
    }

    override fun productDeleted(productId: String) {
        val deletedProduct = state.find { productOnServer ->
            productOnServer.id == productId
        }!!
        state = state.filter { it != deletedProduct }
        observers.map { observer ->
            observer.productDeleted(deletedProduct.toProduct())
        }
    }
}