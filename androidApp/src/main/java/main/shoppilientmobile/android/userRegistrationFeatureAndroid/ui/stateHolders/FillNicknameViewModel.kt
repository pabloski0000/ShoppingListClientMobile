package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.ProcessInformationUiState
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.domain.exceptions.InvalidUserNicknameException
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class FillNicknameViewModel(
    private val registerAdminUseCase: RegisterAdminUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val userRoleRepository: UserRoleRepository,
): ViewModel() {
    private val _processInformationUiState = MutableStateFlow(
        ProcessInformationUiState(
            message = "",
            color = Color.Blue,
        )
    )
    val processInformationUiState = _processInformationUiState.asStateFlow()


    fun registerUser(registration: Registration) {
        try {
            viewModelScope.launch {
                if (registration.role == UserRole.ADMIN) {
                    registerAdminUseCase.registerAdmin(registration.nickname)
                    showTheUserThatTheyAreRegistered()
                } else {
                    registerUserUseCase.registerUser(registration.nickname)
                    showTheUserThatTheyAreRegistered()
                }
            }
            _processInformationUiState.value = ProcessInformationUiState(
                message = "Registering...",
                color = Color.Blue,
            )
        } catch (e: Exception) {
            informUserOfException(e)
        }
    }

    private fun showTheUserThatTheyAreRegistered() {
        _processInformationUiState.value = ProcessInformationUiState(
            message = "Registered",
            color = Color.Green,
        )
    }

    private fun <T> informUserOfException(exception: T) {
        when(exception) {
            is InvalidUserNicknameException -> {
                val userInformationMessage = ProcessInformationUiState(
                    message = "Invalid nickname",
                    color = Color.Red
                )
                _processInformationUiState.value = userInformationMessage
            }
            is RemoteDataSourceException -> {
                val userInformationMessage = ProcessInformationUiState(
                    message = "Remote error. Hold on thirty seconds and try again, sorryyyyy :(",
                    color = Color.Red
                )
                _processInformationUiState.value = userInformationMessage
            }
            else -> {
                _processInformationUiState.value = _processInformationUiState.value.copy(
                    message = "Unexpected error",
                    color = Color.Red,
                )
            }
        }
    }
}