package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.Role
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList
import main.shoppilientmobile.domain.domainExposure.UserRole

class RegisterUserUseCaseImpl(
    private val sharedShoppingList: SharedShoppingList,
    private val userBuilder: UserBuilder,
): RegisterUserUseCase {
    override fun registerUser(registrationData: UserRegistrationData) {
        sharedShoppingList.registerUser(
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