package main.shoppilientmobile.domain

import main.shoppilientmobile.domain.domainExposure.ShoppingList
import main.shoppilientmobile.domain.exceptions.ProductAlreadyExistsException
import main.shoppilientmobile.domain.exceptions.ProductDoesNotExistException

class ShoppingListImpl: ShoppingList {
    private val products: MutableList<Product> = mutableListOf()

    override fun getProducts(): List<Product> {
        return products
    }

    override fun addProduct(product: Product){
        if (products.contains(product))
            throw ProductAlreadyExistsException("This product already exists in the list")
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
}