package main.shoppilientmobile

import main.shoppilientmobile.application.applicationExposure.ExternalSharedShoppingList
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.User

class ExternalSharedShoppingListImpl(
    private val serverApi: ServerApi
): ExternalSharedShoppingList {
    override fun getProducts(): List<Product> {
        TODO("Not yet implemented")
    }

    override fun addProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        TODO("Not yet implemented")
    }

    override fun removeProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun contains(product: Product): Boolean {
        TODO("Not yet implemented")
    }

    override fun registerUser(user: User) {

    }

    override fun getRegisteredUsers(): List<User> {
        TODO("Not yet implemented")
    }

}