package main.shoppilientmobile.userRegistrationFeature.ui

import main.shoppilientmobile.userRegistrationFeature.entities.Registration

interface RegistrationAlertee {
    fun newRegistration(registration: Registration)
}