package main.shoppilientmobile.shoppingList.application

class SynchroniseWithRemoteShoppingListUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) {
    fun synchronise(observer: ShoppingListObserver) {
        remoteShoppingList.observe(observer)
    }
}