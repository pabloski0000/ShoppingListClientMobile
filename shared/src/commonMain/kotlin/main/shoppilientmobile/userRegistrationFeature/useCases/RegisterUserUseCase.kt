package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class RegisterUserUseCase(
    private val registrationRepository: RegistrationRepository,
) {
    suspend fun registerUser(nickname: String) {
        val role = UserRole.BASIC
        registrationRepository.registerUser(
            Registration(
                nickname = nickname,
                role = role,
            )
        )
    }

}