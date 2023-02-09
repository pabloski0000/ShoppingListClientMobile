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
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository

class IntroduceCodeViewModel(
    private val registrationRepository: RegistrationRepository,
    private val navController: NavController,
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
            registrationRepository.confirmRegistration(
                Registration(
                    nickname,
                    UserRole.BASIC,
                    signature,
                )
            )
            navController.navigate(SHOPPING_LIST_ROUTE)
        }
    }
}