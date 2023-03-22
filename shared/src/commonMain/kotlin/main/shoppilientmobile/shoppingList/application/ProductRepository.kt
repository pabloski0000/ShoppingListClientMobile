package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

interface ProductRepository {
    fun add(product: Product)
    fun exists(product: Product): Boolean
}