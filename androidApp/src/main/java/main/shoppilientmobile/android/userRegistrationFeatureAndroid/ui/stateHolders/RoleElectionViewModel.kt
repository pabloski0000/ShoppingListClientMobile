package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository

class RoleElectionViewModel(
    private val userRoleRepository: UserRoleRepository,
): ViewModel() {
    fun onRoleChosen(userRole: UserRole) {
        viewModelScope.launch {
            userRoleRepository.add(userRole)
            println("Stored")
        }
    }
}