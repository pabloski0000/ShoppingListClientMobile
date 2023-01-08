package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

class RegisterAdminUseCaseImpl(
    private val userRepository: UserRepository,
    private val userBuilder: UserBuilder,
): RegisterAdminUseCase {

    override suspend fun registerAdmin(nickname: String): AdminRegistration {
        return userRepository.registerAdmin(
            userBuilder
                .giveItANickname(nickname)
                .setRole(UserRole.ADMIN)
                .build()
        )
    }

}