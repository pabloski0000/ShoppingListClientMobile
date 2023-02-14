package main.shoppilientmobile.shoppingList.domain

import main.shoppilientmobile.domain.Product

interface ProductDeleter {
    fun deleteProducts(products: List<Product>)
    fun deleteProduct(product: Product)
}