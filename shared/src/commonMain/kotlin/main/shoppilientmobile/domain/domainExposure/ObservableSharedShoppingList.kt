package main.shoppilientmobile.domain.domainExposure

interface ObservableSharedShoppingList: SharedShoppingList {
    fun registerObserver(observer: SharedShoppingListObserver)
}