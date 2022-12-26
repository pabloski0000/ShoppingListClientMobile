package main.shoppilientmobile.unitTests.application.mocks

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.api.ShoppingList

class ShoppingListMock: AbstractShoppingListMock(), ShoppingList {

    override fun getProducts(): List<Product> {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        return super.getProductList()
    }

    override fun addProduct(product: Product) {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        super.getProductList().add(product)
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        val index = super.getProductList().indexOf(oldProduct)
        super.getProductList()[index] = newProduct
    }

    override fun removeProduct(product: Product) {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        super.getProductList().remove(product)
    }

    override fun contains(product: Product): Boolean {
        throwExceptionIfRequiredByClientOnNextMethodCall()
        return super.getProductList().contains(product)
    }
}