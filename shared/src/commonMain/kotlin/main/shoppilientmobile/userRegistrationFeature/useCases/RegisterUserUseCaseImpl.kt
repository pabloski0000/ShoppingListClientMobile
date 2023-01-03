package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.Role
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class RegisterUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val userBuilder: UserBuilder,
): RegisterUserUseCase {

    override suspend fun registerUser(registrationData: UserRegistrationData) {
        userRepository.registerUser(
            userBuilder
                .giveItANickname(registrationData.nickname)
                .setRole(adaptUserRole(registrationData.role))
                .build()
        )
    }

    private fun adaptUserRole(userRole: Role): UserRole {
        return when(userRole) {
            Role.ADMIN -> UserRole.ADMIN
            Role.BASIC -> UserRole.BASIC
        }
    }

}