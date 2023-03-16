package main.shoppilientmobile.domain.sharedShoppingList

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.exceptions.TwoProductWithTheSameNameCannotExistException
import main.shoppilientmobile.domain.exceptions.ProductDoesNotExistException

open class SharedShoppingListImpl: SharedShoppingList {
    private val subscribedUsers = mutableListOf<User>()
    private val products = mutableListOf<Product>()

    override fun getProducts(): List<Product> {
        return products
    }

    override fun addProduct(product: Product){
        if (products.contains(product))
            throw TwoProductWithTheSameNameCannotExistException("This product already exists in the list")
        products.add(product)
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        val index = products.indexOf(oldProduct)
        products[index] = newProduct
    }

    override fun removeProduct(product: Product){
        if (! products.remove(product))
            throw ProductDoesNotExistException("The product passed as an argument" +
                    "does not exist in ShoppingList")
        products.remove(product)
    }

    override fun contains(product: Product): Boolean {
        return products.contains(product)
    }

    override fun registerUser(user: User) {
        subscribedUsers.add(user)
    }

    override fun getRegisteredUsers(): List<User> {
        return subscribedUsers.toList()
    }
}