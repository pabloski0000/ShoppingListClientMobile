package main.shoppilientmobile.unitTests.application.mocks

import main.shoppilientmobile.domain.Product

abstract class AbstractShoppingListMock {
    private val productList: MutableList<Product> = mutableListOf()
    private var throwExceptionOnNextMethodCall = false

    protected fun getProductList(): MutableList<Product> {
        return productList
    }

    fun throwExceptionOnNextMethodCall() {
        throwExceptionOnNextMethodCall = true
    }

    protected fun throwExceptionIfRequiredByClientOnNextMethodCall() {
        if (throwExceptionOnNextMethodCall) {
            throwExceptionOnNextMethodCall = false
            throw Exception()
        }
    }
}