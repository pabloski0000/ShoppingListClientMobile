package main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders

import androidx.navigation.NavController
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase

class UserRegistrationViewModelBuilder(
    private val registerUserUseCase: RegisterUserUseCase,
) {
    fun build() = UserRegistrationViewModel(
        registerUserUseCase = registerUserUseCase,
    )
}