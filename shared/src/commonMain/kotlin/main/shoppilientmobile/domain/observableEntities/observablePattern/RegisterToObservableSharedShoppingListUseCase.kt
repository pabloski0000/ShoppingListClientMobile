package main.shoppilientmobile.domain.observableEntities.observablePattern

interface RegisterToObservableSharedShoppingListUseCase {
    fun registerObserver(observer: SharedShoppingListObserver)
}