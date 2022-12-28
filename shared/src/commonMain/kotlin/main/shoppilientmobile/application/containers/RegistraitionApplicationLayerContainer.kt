package main.shoppilientmobile.application.containers

import main.shoppilientmobile.application.ExternalSharedShoppingListSynchronizer
import main.shoppilientmobile.application.RegisterUserUseCaseImpl
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase

object RegistraitionApplicationLayerContainer {
    private val observableSharedShoppingListImpl = ApplicationLayerContainer.observableShoppingListImpl
    val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl(
        sharedShoppingList = observableSharedShoppingListImpl,
        userBuilder = UserBuilderImpl(),
    )
    /*val externalSharedShoppingListSynchronizer = ExternalSharedShoppingListSynchronizer(
        registerObserverToSharedShoppingListUseCase = observableSharedShoppingListImpl,
        externalSharedShoppingList =
    )*/
}