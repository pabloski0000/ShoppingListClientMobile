package main.shoppilientmobile.shoppingList.infrastructure.repositories

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.shoppingList.application.ShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.ServerShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi2

class ServerShoppingList(
    private val serverShoppingListApi: ServerShoppingListApi2,
) : RemoteShoppingList, ServerShoppingListObserver {
    private var observers = emptyList<ShoppingListObserver>()
    private var remoteShoppingListState = emptyList<ProductOnServerShoppingList>()
    private var observingRemoteShoppingList = false

    override fun addProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun observe(observer: ShoppingListObserver) {
        observers = listOf(*observers.toTypedArray(), observer)
        if (! observingRemoteShoppingList) {
            serverShoppingListApi.observeServerShoppingList(this)
            observingRemoteShoppingList = true
        }
        observer.currentState(
            remoteShoppingListState.map { productOnServer -> productOnServer.toProduct() }
        )
    }

    override fun currentState(product: ProductOnServerShoppingList) {
        remoteShoppingListState = listOf(
            *remoteShoppingListState.toTypedArray(),
            product,
        )
        observers.map { observer ->
            observer.currentState(
                remoteShoppingListState.map { productOnServer -> productOnServer.toProduct() }
            )
        }
    }

    override fun productAdded(product: ProductOnServerShoppingList) {
        remoteShoppingListState = listOf(
            *remoteShoppingListState.toTypedArray(),
            product,
        )
        observers.map { observer ->
            observer.productAdded(product.toProduct())
        }
    }

    override fun productModified(modifiedProduct: ProductOnServerShoppingList) {
        val oldProduct = remoteShoppingListState.find { productOnServer ->
            productOnServer == modifiedProduct
        }!!
        remoteShoppingListState = remoteShoppingListState.map { productOnServer ->
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
        val deletedProduct = remoteShoppingListState.find { productOnServer ->
            productOnServer.id == productId
        }!!
        remoteShoppingListState = remoteShoppingListState.map { productOnServer ->
            if (productOnServer == deletedProduct) {
                return@map deletedProduct
            }
            return@map productOnServer
        }
        observers.map { observer ->
            observer.productDeleted(deletedProduct.toProduct())
        }
    }
}