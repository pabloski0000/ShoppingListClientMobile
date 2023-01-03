package main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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
    private val userInformationMessageUiState = mutableStateOf(UserInformationMessageUiState(
        message = "",
        color = Color.Blue,
    ))


    override fun onRoleChosen(role: Role) {
        userRole = role
        navController?.navigate(FillNicknameRoutableComposable.route)
    }

    override fun onNicknameIntroduced(nickname: String) {
        userNickname = nickname
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            informUserDependingOnTheException(throwable)
        }
        runBlocking(coroutineExceptionHandler) {
            async {
                registerUser()
            }
            userInformationMessageUiState.value = UserInformationMessageUiState(
                message = "Registering...",
                color = Color.Blue,
            )
        }
    }

    private suspend fun registerUser() {
        registerUserUseCase.registerUser(
            UserRegistrationData(userNickname, userRole)
        )
    }

    override fun getUserInformationMessage(): State<UserInformationMessageUiState> {
        return userInformationMessageUiState
    }

    fun setNavController(_navController: NavController) {
        navController = _navController
    }

    private fun <T> informUserDependingOnTheException(exception: T) {
        when(exception) {
            is InvalidUserNicknameException -> {
                val userInformationMessage = UserInformationMessageUiState(
                    message = "Invalid nickname",
                    color = Color.Red
                )
                userInformationMessageUiState.value = userInformationMessage
            }
            is RemoteDataSourceException -> {
                val userInformationMessage = UserInformationMessageUiState(
                    message = "Remote error. Hold on thirty seconds and try again, sorryyyyy :(",
                    color = Color.Red
                )
                userInformationMessageUiState.value = userInformationMessage
            }
        }
    }
}

data class UserInformationMessageUiState(val message: String, val color: Color)
