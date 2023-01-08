package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository

class ListenToNewUserRegistrationsUseCase(
    private val registrationRepository: RegistrationRepository,
) {
    fun listen() = registrationRepository.
}