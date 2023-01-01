package main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.RoleElectionViewModel
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.Role
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.domain.exceptions.InvalidUserNicknameException
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException

class UserRegistrationViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
): ViewModel(), RoleElectionViewModel, FillNicknameViewModel {
    companion object {
        const val firstScreenRoute = RoleElectionRoutableComposable.route
    }
    private var navController: NavController? = null
    private lateinit var userNickname: String
    private lateinit var userRole: Role
    private var errorMessage = mutableStateOf("")


    override fun onRoleChosen(role: Role) {
        userRole = role
        navController?.navigate(FillNicknameRoutableComposable.route)
    }

    override fun onNicknameIntroduced(nickname: String) {
        userNickname = nickname
        registerUser()
    }

    fun registerUser() {
        informUserOfResult {
            registerUserUseCase.registerUser(
                UserRegistrationData(userNickname, userRole)
            )
        }
    }

    fun getErrorMessage(): MutableState<String> {
        return errorMessage
    }

    fun setNavController(_navController: NavController) {
        navController = _navController
    }

    private fun informUserOfResult(task: () -> Unit) {
        try {
            task()
            errorMessage.value = ""
        } catch (e: InvalidUserNicknameException) {
            errorMessage.value = "Invalid nickname"
        } catch (e: RemoteDataSourceException) {
            errorMessage.value = "Remote error. Hold on thirty seconds and try again, sorryyyyy :("
        }
    }
}