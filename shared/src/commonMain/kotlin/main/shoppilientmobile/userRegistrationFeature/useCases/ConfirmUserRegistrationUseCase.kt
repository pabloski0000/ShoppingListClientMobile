package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.exceptions.WrongCodeException
import kotlin.coroutines.cancellation.CancellationException

class ConfirmUserRegistrationUseCase(
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository,
) {
    @Throws(CancellationException::class, WrongCodeException::class)
    suspend fun confirmRegistration(nickname: String, code: String) {
        registrationRepository.confirmRegistration(
            Registration(
                nickname,
                UserRole.BASIC,
                code,
            )
        )
        userRepository.saveLocalUser(
            UserBuilderImpl().giveItANickname(nickname)
                .setRole(UserRole.ADMIN)
                .build()
        )
    }
}