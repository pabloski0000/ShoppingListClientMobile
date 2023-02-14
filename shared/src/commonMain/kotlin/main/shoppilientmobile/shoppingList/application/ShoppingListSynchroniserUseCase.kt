package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.domain.ShoppingList

class ShoppingListSynchroniserUseCase(
    private val serverShoppingListRemoteDataSource: ServerShoppingListRemoteDataSource,
    private val shoppingList: ShoppingList,
) : ServerShoppingListObserver {
    fun synchroniseWithServerShoppingList() {
        serverShoppingListRemoteDataSource.observeServerShoppingList(this)
    }

    override fun stateAtTheMomentOfSubscribing(products: List<Product>) {
        shoppingList.recreate(products)
    }

    override fun productAdded(product: Product) {
        shoppingList.addProduct(product)
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        shoppingList.modifyProduct(oldProduct, newProduct)
    }
}