package main.shoppilientmobile.shoppingList.infrastructure.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.exceptions.ProductDescriptionExceedsMaximumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionIsShorterThanMinimumLengthException
import main.shoppilientmobile.domain.exceptions.ThereCannotBeTwoProductsWithTheSameNameException
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.shoppingList.application.RequestExceptionListener
import main.shoppilientmobile.shoppingList.application.SharedShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.ServerShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi

class ServerShoppingList(
    private val serverShoppingListApi: ServerShoppingListApi,
) : RemoteShoppingList, ServerShoppingListObserver {
    private var observers = setOf<SharedShoppingListObserver>()
    private var state = emptyList<ProductOnServerShoppingList>()
    private var currentlySubscribedToSharedShoppingList = false

    override suspend fun addProduct(product: Product, exceptionListener: RequestExceptionListener) {
        try {
            serverShoppingListApi.addProduct(ProductOnServerShoppingList.fromProduct(product))
        }catch (e: ThereCannotBeTwoProductsWithTheSameNameException) {
            exceptionListener.informUserOfError("Este producto ya existe en la lista")
        } catch (e: ProductDescriptionExceedsMaximumLengthException) {
            exceptionListener.informUserOfError("Es demasiado largo. Acórtalo")
        } catch (e: ProductDescriptionIsShorterThanMinimumLengthException) {
            exceptionListener.informUserOfError("Es demasiado corto. Alárgalo")
        }
    }

    override suspend fun modifyProduct(oldProduct: Product, newProduct: Product) {
        val productToModify = state.find { it.toProduct() == oldProduct }!!
        val modifiedProduct = productToModify.copy(description = newProduct.description)
        serverShoppingListApi.modifyProduct(modifiedProduct)
    }

    override suspend fun deleteProduct(product: Product) {
        val productToDelete = state.find { it.toProduct() == product }!!
        serverShoppingListApi.deleteProduct(productToDelete)
    }

    override suspend fun deleteAllProducts() {
        serverShoppingListApi.deleteAllProducts()
    }

    override fun subscribe(observer: SharedShoppingListObserver) {
        if (! currentlySubscribedToSharedShoppingList) {
            serverShoppingListApi.subscribeToSharedShoppingList(this)
            currentlySubscribedToSharedShoppingList = true
        }
        observers = setOf(*observers.toTypedArray(), observer)
        observer.currentState(
            state.map { productOnServer -> productOnServer.toProduct() }
        )
    }

    override fun listen() {
        serverShoppingListApi.listenToSharedShoppingList()
    }

    override suspend fun stopListening() {
        serverShoppingListApi.stopListeningToSharedShoppingList()
        state = emptyList()
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