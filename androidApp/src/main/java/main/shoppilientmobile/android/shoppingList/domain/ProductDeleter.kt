package main.shoppilientmobile.android.shoppingList.domain

import main.shoppilientmobile.domain.Product

interface ProductDeleter {
    fun deleteProducts(products: List<Product>)
    fun deleteProduct(product: Product)
}