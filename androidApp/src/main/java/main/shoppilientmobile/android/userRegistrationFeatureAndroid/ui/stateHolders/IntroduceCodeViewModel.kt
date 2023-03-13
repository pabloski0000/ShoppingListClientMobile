package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.shoppingList.presentation.SHOPPING_LIST_ROUTE
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.ProcessInformationUiState
import main.shoppilientmobile.userRegistrationFeature.entities.RegistrationCode
import main.shoppilientmobile.userRegistrationFeature.useCases.ConfirmUserRegistrationUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.exceptions.WrongCodeException

class IntroduceCodeViewModel(
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

    fun confirmRegistration(nickname: String, code: String) {
        viewModelScope.launch {
            try {
                confirmUserRegistrationUseCase.confirmRegistration(nickname, code.toInt())
                navController.navigate(SHOPPING_LIST_ROUTE)
            } catch (e: WrongCodeException) {
                _processInformationUiState.update {
                    ProcessInformationUiState(
                        message = "Wrong code",
                        color = Color.Red,
                    )
                }
            } catch (e: NumberFormatException) {
                _processInformationUiState.update {
                    ProcessInformationUiState(
                        message = "Beware, your code is wrong. Probably it is too long or it" +
                                " contains non-numerical characters.",
                        color = Color.Red,
                    )
                }
            }
        }
    }
}