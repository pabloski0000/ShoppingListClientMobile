package main.shoppilientmobile.unitTests.application.mocks

import main.shoppilientmobile.application.repositories.ShoppingListRepository
import main.shoppilientmobile.domain.Product

class ShoppingListRepositoryMock: AbstractShoppingListMock(), ShoppingListRepository {

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
}