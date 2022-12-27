package main.shoppilientmobile.domain.observableEntities

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ObservableSharedShoppingList
import main.shoppilientmobile.domain.sharedShoppingList.SharedShoppingListImpl
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.observableEntities.observablePattern.SharedShoppingListObserver

class ObservableSharedShoppingListImpl: SharedShoppingListImpl(), ObservableSharedShoppingList {
    private val observers: MutableList<SharedShoppingListObserver> = mutableListOf()

    override fun registerObserver(observer: SharedShoppingListObserver) {
        observers.add(observer)
    }

    override fun addProduct(product: Product) {
        super.addProduct(product)
        notifyObservers {
            it.productAdded(product)
        }
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        super.modifyProduct(oldProduct, newProduct)
        notifyObservers {
            it.productModified(oldProduct, newProduct)
        }
    }

    override fun removeProduct(product: Product) {
        super.removeProduct(product)
        notifyObservers {
            it.productRemoved(product)
        }
    }

    override fun registerUser(user: User) {
        super.registerUser(user)
        notifyObservers {
            it.userRegistered(user)
        }
    }

    private fun notifyObservers(notification: (observer: SharedShoppingListObserver) -> Unit) {
        observers.forEach(notification)
    }
}