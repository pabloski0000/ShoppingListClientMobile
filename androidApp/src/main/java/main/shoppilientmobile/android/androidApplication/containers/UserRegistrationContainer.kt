package main.shoppilientmobile.android.androidApplication.containers

import main.shoppilientmobile.android.screens.FillingNicknameScreen
import main.shoppilientmobile.android.screens.RoleElectionScreen
import main.shoppilientmobile.android.screensLogic.UserRegistrationLogic
import main.shoppilientmobile.application.containers.UserRegistraitionApplicationLayerContainer

class UserRegistrationContainer {
    val userRegistrationScreenLogic = UserRegistrationLogic(
        roleElectionScreen = RoleElectionScreen(),
        fillingNicknameScreen = FillingNicknameScreen(),
        registerUserUseCase = UserRegistraitionApplicationLayerContainer.registerUserUseCase,
    )
}