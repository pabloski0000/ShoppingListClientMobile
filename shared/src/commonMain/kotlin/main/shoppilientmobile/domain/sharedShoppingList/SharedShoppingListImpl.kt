package main.shoppilientmobile.domain.sharedShoppingList

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ObservableSharedShoppingList
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.exceptions.ProductAlreadyExistsException
import main.shoppilientmobile.domain.exceptions.ProductDoesNotExistException
import main.shoppilientmobile.domain.domainExposure.SharedShoppingListObserver

open class SharedShoppingListImpl: ObservableSharedShoppingList {
    private val observers: MutableList<SharedShoppingListObserver> = mutableListOf()
    private val subscribedUsers = mutableListOf<User>()
    private val products = mutableListOf<Product>()

    override fun registerObserver(observer: SharedShoppingListObserver) {
        observers.add(observer)
    }

    override fun getProducts(): List<Product> {
        return products
    }

    override fun addProduct(product: Product){
        if (products.contains(product))
            throw ProductAlreadyExistsException("This product already exists in the list")
        products.add(product)
        notifyObservers {
            it.productAdded(product)
        }
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        val index = products.indexOf(oldProduct)
        products[index] = newProduct
        notifyObservers {
            it.productModified(oldProduct, newProduct)
        }
    }

    override fun removeProduct(product: Product){
        if (! products.remove(product))
            throw ProductDoesNotExistException("The product passed as an argument" +
                    "does not exist in ShoppingList")
        products.remove(product)
        notifyObservers {
            it.productRemoved(product)
        }
    }

    override fun contains(product: Product): Boolean {
        return products.contains(product)
    }

    override fun registerUser(user: User) {
        subscribedUsers.add(user)
        notifyObservers {
            it.userRegistered(user)
        }
    }

    override fun getRegisteredUsers(): List<User> {
        return subscribedUsers.toList()
    }

    private fun notifyObservers(notification: (observer: SharedShoppingListObserver) -> Unit) {
        observers.forEach(notification)
    }
}