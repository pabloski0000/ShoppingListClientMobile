package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.shoppingList.application.ShoppingListSynchroniserUseCase
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class RegisterAdminUseCaseImpl(
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository,
    private val shoppingListSynchroniserUseCase: ShoppingListSynchroniserUseCase,
): RegisterAdminUseCase {

    override suspend fun registerAdmin(nickname: String) {
        val user = registrationRepository.registerAdmin(Registration(nickname, UserRole.ADMIN))
        userRepository.saveLocalUser(user)
        shoppingListSynchroniserUseCase.synchroniseWithServerShoppingList()
    }

}