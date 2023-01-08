package main.shoppilientmobile.userRegistrationFeature.useCases

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.exceptions.NotAuthorizedException

class ListenToRegistrationsUseCase(
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository,
) {
    suspend fun listen(): Flow<Registration> {
        if (userRepository.getLocalUser().getRole() != UserRole.ADMIN) {
            throw NotAuthorizedException("You need to be admin for this use case")
        }
        return registrationRepository.listenToRegistrations()
    }
}