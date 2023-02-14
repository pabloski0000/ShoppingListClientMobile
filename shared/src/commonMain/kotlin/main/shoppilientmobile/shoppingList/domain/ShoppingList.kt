package main.shoppilientmobile.shoppingList.domain

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.domain.Product

interface ShoppingList : ProductDeleter {
    fun recreate(products: List<Product>)
    fun addProduct(product: Product)
    fun getProducts(): List<Product>
    fun modifyProduct(oldProduct: Product, newProduct: Product)
    fun observe(): Flow<List<Product>>
    fun observeShoppingList(observer: ShoppingListObserver)
}