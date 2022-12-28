package main.shoppilientmobile.application.containers

import main.shoppilientmobile.domain.observableEntities.ObservableSharedShoppingListImpl
import main.shoppilientmobile.domain.sharedShoppingList.SharedShoppingListImpl

object ApplicationLayerContainer {
    val observableShoppingListImpl = ObservableSharedShoppingListImpl()
}