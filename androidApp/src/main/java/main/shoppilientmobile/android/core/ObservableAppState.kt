package main.shoppilientmobile.android.core

import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.shoppingList.application.AppStateObserver

class ObservableAppState(
    private val appStateObserver: AppStateObserver,
) {
    fun onStart() {
        appStateObserver.appHasChangedToTheForeground()
    }

    fun onStop() {
        runBlocking {
            appStateObserver.appHasChangedToTheBackground()
        }
    }
}