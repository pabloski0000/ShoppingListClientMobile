package main.shoppilientmobile.android.core

import main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.FillingNicknameScreen
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.RoleElectionScreen
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.featuresLogic.UserRegistrationLogic
import main.shoppilientmobile.userRegistrationFeature.containers.UserRegistrationFeatureContainer

class AndroidContainer {
    private val userRegistrationFeatureContainer = UserRegistrationFeatureContainer()
    val userRegistrationLogic = UserRegistrationLogic(
        roleElectionScreen = RoleElectionScreen(),
        fillingNicknameScreen = FillingNicknameScreen(),
        registerUserUseCase = userRegistrationFeatureContainer.registerUserUseCase
    )
}