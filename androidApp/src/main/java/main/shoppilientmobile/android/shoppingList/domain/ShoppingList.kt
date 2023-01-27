package main.shoppilientmobile.android.shoppingList.domain

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.domain.Product

interface ShoppingList {
    fun addProduct(product: Product)
    fun getProducts(): List<Product>
    fun modifyProduct(oldProduct: Product, newProduct: Product)
    fun deleteProducts(products: List<Product>)
    fun observe(): Flow<List<Product>>
    fun observeShoppingList(observer: ShoppingListObserver)
}