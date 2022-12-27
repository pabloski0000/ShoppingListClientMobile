package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.UserRole
import main.shoppilientmobile.domain.domainExposure.SharedShoppingList
import main.shoppilientmobile.domain.domainExposure.User

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

    private fun adaptUserRole(userRole: UserRole): User.Role {
        return when(userRole) {
            UserRole.ADMIN -> User.Role.ADMIN
            UserRole.BASIC -> User.Role.BASIC
        }
    }

}