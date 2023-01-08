package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegisterUserUseCase(
    private val registrationRepository: RegistrationRepository,
) {
    suspend fun registerUser(nickname: String) {
        registrationRepository.registerUser(Registration(
            nickname = nickname,
            role = UserRole.BASIC,
        ))
    }
}