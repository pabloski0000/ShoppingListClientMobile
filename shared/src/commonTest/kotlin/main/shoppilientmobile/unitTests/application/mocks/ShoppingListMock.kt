package main.shoppilientmobile.unitTests.application.mocks

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ShoppingList
import main.shoppilientmobile.domain.domainExposure.User

open class ShoppingListMock: ShoppingList {
    private val subscribedUsers = mutableListOf<User>()
    private val productList: MutableList<Product> = mutableListOf()
    private var throwExceptionOnNextMethodCall = false


    override fun getProducts(): List<Product> {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        return productList
    }

    override fun addProduct(product: Product) {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        productList.add(product)
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        val index = productList.indexOf(oldProduct)
        productList[index] = newProduct
    }

    override fun removeProduct(product: Product) {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        productList.remove(product)
    }

    override fun contains(product: Product): Boolean {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        return productList.contains(product)
    }

    override fun registerUser(user: User) {
        subscribedUsers.add(user)
    }

    override fun getRegisteredUsers(): List<User> {
        return subscribedUsers.toList()
    }

    fun throwExceptionOnNextMethodCall() {
        throwExceptionOnNextMethodCall = true
    }

    private fun throwExceptionIfRequiredByClientOnNextMethodCall() {
        if (throwExceptionOnNextMethodCall) {
            throwExceptionOnNextMethodCall = false
            throw Exception()
        }
    }
}