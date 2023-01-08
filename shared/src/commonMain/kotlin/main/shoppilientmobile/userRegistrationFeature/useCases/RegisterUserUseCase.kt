package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val registrationRepository: RegistrationRepository,
    private val userBuilder: UserBuilder,
) {
    suspend fun registerUser(nickname: String): confirmRegistrationWithSecurityCode {
        userRepository.registerUser(
            userBuilder.giveItANickname(nickname)
                .setRole(UserRole.BASIC)
                .build()
        )
        return { code ->
            registrationRepository.confirmUserRegistration(
                Registration(nickname, code)
            )
        }
    }
}