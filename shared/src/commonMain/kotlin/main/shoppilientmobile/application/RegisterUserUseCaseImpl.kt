package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.UserRole
import main.shoppilientmobile.application.applicationExposure.repositories.ShoppingListRepository
import main.shoppilientmobile.domain.domainExposure.ShoppingList
import main.shoppilientmobile.domain.domainExposure.User

class RegisterUserUseCaseImpl(
    private val shoppingList: ShoppingList,
    private val shoppingListRepository: ShoppingListRepository,
    private val userBuilder: UserBuilder
): RegisterUserUseCase {
    override fun registerUser(registrationData: UserRegistrationData) {
        val user = userBuilder
            .giveItANickname(registrationData.nickname)
            .setRole(adaptUserRole(registrationData.role))
            .build()
        shoppingList.registerUser(user)
        shoppingListRepository.registerUser(user)
    }

    private fun adaptUserRole(userRole: UserRole): User.Role {
        return when(userRole) {
            UserRole.ADMIN -> User.Role.ADMIN
            UserRole.BASIC -> User.Role.BASIC
        }
    }

}