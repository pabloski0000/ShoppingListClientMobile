package main.shoppilientmobile.shoppingList.application

class SynchroniseWithRemoteShoppingListUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) : AppStateObserver {
    fun synchronise(observer: SharedShoppingListObserver) {
        remoteShoppingList.listen()
        remoteShoppingList.subscribe(observer)
    }

    override fun appHasChangedToTheForeground() {
        remoteShoppingList.listen()
        println("Shopping list moved to the foreground")
    }

    override suspend fun appHasChangedToTheBackground() {
        remoteShoppingList.stopListening()
        println("Shopping list moved to the background")
    }
}