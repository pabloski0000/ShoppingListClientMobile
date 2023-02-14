package main.shoppilientmobile.shoppingList.application

interface ServerShoppingListRemoteDataSource {
    fun observeServerShoppingList(observer: ServerShoppingListObserver)
}