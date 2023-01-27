package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.ProcessInformationUiState
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository

class IntroduceCodeViewModel(
    private val registrationRepository: RegistrationRepository,
) : ViewModel() {
    private val _processInformationUiState = MutableStateFlow(
        ProcessInformationUiState(
            message = "",
            color = Color.Blue,
        )
    )
    val processInformationUiState = _processInformationUiState.asStateFlow()

    fun onSignatureIntroduced(signature: String) {
        TODO()
    }
}