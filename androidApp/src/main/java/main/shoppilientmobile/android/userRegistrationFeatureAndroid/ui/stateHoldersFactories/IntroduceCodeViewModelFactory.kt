package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository

class IntroduceCodeViewModelFactory(
    private val registrationRepository: RegistrationRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IntroduceCodeViewModel(registrationRepository) as T
    }
}