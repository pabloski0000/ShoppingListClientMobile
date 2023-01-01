package main.shoppilientmobile.android.core

import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModelBuilder
import main.shoppilientmobile.userRegistrationFeature.containers.UserRegistrationFeatureContainer

class AndroidContainer {
    private val userRegistrationFeatureContainer = UserRegistrationFeatureContainer()
    val userRegistrationViewModel = UserRegistrationViewModel(
        registerUserUseCase = userRegistrationFeatureContainer.registerUserUseCase,
    )
}