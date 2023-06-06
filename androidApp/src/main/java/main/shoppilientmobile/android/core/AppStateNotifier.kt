package main.shoppilientmobile.android.core

import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.shoppingList.application.SynchroniseWithRemoteShoppingListUseCase

class AppStateNotifier(
    private val synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase,
) {
    fun onStart() {
        synchroniseWithRemoteShoppingListUseCase.continueSynchronisation()
    }

    fun onStop() {
        runBlocking {
            synchroniseWithRemoteShoppingListUseCase.stopSynchronisation()
        }
    }
}