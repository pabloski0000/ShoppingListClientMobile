package main.shoppilientmobile.android.core

import android.content.Context
import main.shoppilientmobile.android.createListFeature.stateHolders.ShoppingListViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModel
import main.shoppilientmobile.userRegistrationFeature.containers.SharedAndroidContainer

class AndroidContainer(
    private val context: Context,
) {
    private val sharedAndroidContainer = SharedAndroidContainer(
        context = context
    )
    val userRegistrationViewModel = UserRegistrationViewModel(
        registerAdminUseCase = sharedAndroidContainer.registerAdminUseCase,
        registerUserUseCase = sharedAndroidContainer.registerUserUseCase,
    )
    val shoppingListViewModel = ShoppingListViewModel(
        productRepository = sharedAndroidContainer.productRepository,
    )
}