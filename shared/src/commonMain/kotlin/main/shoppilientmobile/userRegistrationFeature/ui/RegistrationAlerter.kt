package main.shoppilientmobile.userRegistrationFeature.ui

import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.useCases.ListenToRegistrationsUseCase

class RegistrationAlerter(
    private val listenToRegistrationsUseCase: ListenToRegistrationsUseCase,
) {
    private val alertees: MutableList<RegistrationAlertee> = mutableListOf()
    private var listening = false

    suspend fun listen(alertee: RegistrationAlertee) {
        alertees.add(alertee)
        if (! listening) {
            listenToRegistrationsUseCase.listen().collect { registration ->
                alert(registration)
            }
            listening = true
        }
    }

    private fun alert(registration: Registration) {
        alertees.map { it.newRegistration(registration) }
    }
}