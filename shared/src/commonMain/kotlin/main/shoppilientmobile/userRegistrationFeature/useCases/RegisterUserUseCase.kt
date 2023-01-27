package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class RegisterUserUseCase(
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository,
) {
    suspend fun registerUser(nickname: String): RegistrationValidator {
        val role = UserRole.BASIC
        registrationRepository.registerUser(
            Registration(
                nickname = nickname,
                role = role,
            )
        )
        return object : RegistrationValidator {
            override suspend fun confirmRegistration(code: String): Boolean {
                val user = registrationRepository.confirmRegistration(
                    Registration(
                        nickname,
                        role,
                        code,
                    )
                )
                userRepository.saveLocalUser(user)
                return true
            }
        }
    }

}