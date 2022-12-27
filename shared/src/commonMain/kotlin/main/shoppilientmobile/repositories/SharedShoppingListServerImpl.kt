package main.shoppilientmobile.repositories

import main.shoppilientmobile.ApiServer
import main.shoppilientmobile.application.applicationExposure.repositories.ServerSharedShoppingList
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.User

class SharedShoppingListServerImpl(
    private val apiServer: ApiServer
): ServerSharedShoppingList {

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
        when(user.getRole()) {
            User.Role.BASIC -> apiServer.registerBasicUser(user)
            User.Role.ADMIN -> apiServer.registerAdminUser(user)
        }
    }

    override fun getRegisteredUsers(): List<User> {
        TODO("Not yet implemented")
    }


}