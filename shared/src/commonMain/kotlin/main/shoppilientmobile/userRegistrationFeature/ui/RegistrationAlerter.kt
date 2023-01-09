package main.shoppilientmobile.userRegistrationFeature.ui

import main.shoppilientmobile.userRegistrationFeature.useCases.ListenToRegistrationsUseCase

class RegistrationAlerter(
    private val listenToRegistrationsUseCase: ListenToRegistrationsUseCase,
    private val alertee: RegistrationAlertee,
) {

    suspend fun alert() {
        listenToRegistrationsUseCase.listen().collect { registration ->
            alertee.newRegistration(registration)
        }
    }
}