package main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.*
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.application.applicationExposure.Role
import main.shoppilientmobile.domain.exceptions.InvalidUserNicknameException
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.confirmRegistrationWithSecurityCode

class UserRegistrationViewModel(
    private val registerAdminUseCase: RegisterAdminUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
): ViewModel(), RoleElectionViewModel, FillNicknameViewModel {
    private val roleElectionComposableRoute = RoleElectionRoutableComposable.route
    private val fillNicknameComposableRoute = FillNicknameRoutableComposable.route
    private val introduceUserSecurityCodeComposableRoute = IntroduceUserRegistrationCodeRoutableComposable.route
    private lateinit var confirmUserRegistration: confirmRegistrationWithSecurityCode
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

    override suspend fun onNicknameIntroduced(nickname: String) {
        userNickname = nickname
        try {
            coroutineScope {
                val result = launch {
                    if (userRole == Role.BASIC) {
                        confirmUserRegistration = registerUserUseCase.registerUser(userNickname)
                    } else {
                        registerAdminUseCase.registerAdmin(userNickname)
                    }
                }
                userInformationMessageUiState.value = UserInformationMessageUiState(
                    message = "Registering...",
                    color = Color.Blue,
                )
                result.join()
                if (userRole == Role.ADMIN) {
                    userInformationMessageUiState.value = UserInformationMessageUiState(
                        message = "Registered",
                        color = Color.Green,
                    )
                } else {
                    navigateTo(introduceUserSecurityCodeComposableRoute)
                }
            }
        } catch (e: Exception) {
            informUser(e)
        }
    }

    suspend fun onCodeIntroduced(securityCode: String) {
        try {
            confirmUserRegistration(securityCode)
            userInformationMessageUiState.value = UserInformationMessageUiState(
                message = "Registered",
                color = Color.Green,
            )
        } catch (e: Exception) {
            userInformationMessageUiState.value = UserInformationMessageUiState(
                message = "Check out your security code if it continues failing" +
                        " there may be a network problem",
                color = Color.Red,
            )
        }
    }

    override fun getUserInformationMessage(): State<UserInformationMessageUiState> {
        return userInformationMessageUiState
    }

    fun setNavController(_navController: NavController) {
        navController = _navController
    }

    private fun <T> informUser(exception: T) {
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
            else -> {
                userInformationMessageUiState.value = userInformationMessageUiState.value.copy(
                    message = "Unexpected error",
                    color = Color.Red,
                )
            }
        }
    }

    private fun navigateTo(composableRoute: String) {
        navController!!.navigate(composableRoute)
        userInformationMessageUiState.value = UserInformationMessageUiState(
            message = ""
        )
    }
}

data class UserInformationMessageUiState(val message: String, val color: Color = Color.Red)
