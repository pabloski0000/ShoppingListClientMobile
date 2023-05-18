package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.domain.ShoppingList

class ShoppingListSynchroniserUseCase(
    private val remoteShoppingList: RemoteShoppingList,
    private val shoppingList: ShoppingList,
) : SharedShoppingListObserver {
    fun synchroniseWithExternalShoppingList() {
        remoteShoppingList.subscribe(this)
    }

    override fun currentState(products: List<Product>) {
        shoppingList.recreate(products)
    }

    override fun productAdded(product: Product) {
        shoppingList.addProduct(product)
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        shoppingList.modifyProduct(oldProduct, newProduct)
    }

    override fun productDeleted(product: Product) {
        shoppingList.deleteProduct(product)
    }
}