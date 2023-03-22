package main.shoppilientmobile.shoppingList.application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class ListenToUserRegistrationsUseCase(
    private val userRepository: UserRepository,
    private val registrationRepository: RegistrationRepository,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun listen(userRegistrationsListener: UserRegistrationsListener) {
        if (userRepository.getLocalUser().getRole() == UserRole.ADMIN) {
            coroutineScope.launch(Dispatchers.Default) {
                registrationRepository.listenToUserRegistrations(userRegistrationsListener)
            }
        }
    }
}