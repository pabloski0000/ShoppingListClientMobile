package main.shoppilientmobile.android.shoppingList.presentation

interface ShoppingListObserver {
    fun currentState(products: List<Product>)
    fun productAdded(product: Product)
    fun productModified(oldProduct: Product, newProduct: Product)
    fun productDeleted(product: Product)
}