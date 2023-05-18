package main.shoppilientmobile.shoppingList.application

interface AppStateObserver {
    fun appHasChangedToTheForeground()
    suspend fun appHasChangedToTheBackground()
}