package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository

class IntroduceCodeViewModelFactory(
    private val registrationRepository: RegistrationRepository,
    private val navController: NavController,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IntroduceCodeViewModel(registrationRepository, navController) as T
    }
}