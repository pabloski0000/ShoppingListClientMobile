package main.shoppilientmobile.application.containers

import main.shoppilientmobile.application.RegisterUserUseCaseImpl
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase

object UserRegistraitionApplicationLayerContainer {
    private val observableSharedShoppingListImpl = ApplicationLayerContainer.observableShoppingListImpl
    val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl(
        sharedShoppingList = observableSharedShoppingListImpl,
        userBuilder = UserBuilderImpl(),
    )
}