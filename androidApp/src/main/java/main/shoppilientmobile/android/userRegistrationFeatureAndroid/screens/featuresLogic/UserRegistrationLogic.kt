package main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.featuresLogic

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.FillingNicknameScreen
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.RoleElectionScreen
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.Role
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData

class UserRegistrationLogic(
    private val roleElectionScreen: RoleElectionScreen,
    private val fillingNicknameScreen: FillingNicknameScreen,
    private val registerUserUseCase: RegisterUserUseCase,
) {

    @Composable
    fun displayComposable() {
        BuildNavHost()
    }

    @Composable
    private fun BuildNavHost() {
        val navController = rememberNavController()
        var role: Role? = null
        NavHost(
            navController = navController,
            startDestination = roleElectionScreen.route,
        ) {
            composable(route = roleElectionScreen.route) {
                roleElectionScreen.ChooseAdminOrUserRoleScreen {
                    role = it
                    navController.navigate(fillingNicknameScreen.route)
                }
            }

            composable(route = fillingNicknameScreen.route) {
                fillingNicknameScreen.IntroduceNicknameScreen { nickname ->
                    runBlocking {
                        registerUserUseCase.registerUser(
                            UserRegistrationData(nickname, role!!)
                        )
                    }
                }
            }
        }
    }
}