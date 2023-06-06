package main.shoppilientmobile.shoppingList.application

class SynchroniseWithRemoteShoppingListUseCase(
    private val remoteShoppingList: RemoteShoppingList,
) {
    fun synchronise(observer: SharedShoppingListObserver) {
        remoteShoppingList.listen()
        remoteShoppingList.subscribe(observer)
    }

    /**
     * It is idempotent
     */
    fun continueSynchronisation() {
        remoteShoppingList.listen()
        println("Shopping list moved to the foreground")
    }

    /**
     * It is idempotent
     */
    suspend fun stopSynchronisation() {
        remoteShoppingList.stopListening()
        println("Shopping list moved to the background")
    }
}