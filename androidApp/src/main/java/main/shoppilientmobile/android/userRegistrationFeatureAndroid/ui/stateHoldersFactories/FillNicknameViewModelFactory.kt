package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class FillNicknameViewModelFactory(
    private val registerAdminUseCase: RegisterAdminUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val userRoleRepository: UserRoleRepository,
    private val navController: NavController,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FillNicknameViewModel(
            registerAdminUseCase,
            registerUserUseCase,
            userRoleRepository,
            navController
        ) as T
    }
}