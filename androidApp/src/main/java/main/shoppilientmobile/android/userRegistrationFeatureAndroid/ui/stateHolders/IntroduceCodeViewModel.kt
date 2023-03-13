package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.shoppingList.presentation.SHOPPING_LIST_ROUTE
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.ProcessInformationUiState
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.ConfirmUserRegistrationUseCase

class IntroduceCodeViewModel(
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository,
    private val navController: NavController,
    private val confirmUserRegistrationUseCase: ConfirmUserRegistrationUseCase,
) : ViewModel() {
    private val _processInformationUiState = MutableStateFlow(
        ProcessInformationUiState(
            message = "",
            color = Color.Blue,
        )
    )
    val processInformationUiState = _processInformationUiState.asStateFlow()

    fun confirmRegistration(nickname: String, signature: String) {
        viewModelScope.launch {
            confirmUserRegistrationUseCase.confirmRegistration(nickname, code = signature)
            navController.navigate(SHOPPING_LIST_ROUTE)
        }
    }
}