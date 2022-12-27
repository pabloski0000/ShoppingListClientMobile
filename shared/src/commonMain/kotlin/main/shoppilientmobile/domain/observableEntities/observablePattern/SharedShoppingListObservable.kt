package main.shoppilientmobile.domain.observableEntities.observablePattern

interface SharedShoppingListObservable {
    fun registerObserver(observer: SharedShoppingListObserver)
}