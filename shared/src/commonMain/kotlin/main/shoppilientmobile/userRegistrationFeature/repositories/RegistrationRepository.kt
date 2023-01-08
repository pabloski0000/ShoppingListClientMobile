package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.userRegistrationFeature.entities.Registration

interface RegistrationRepository {
    suspend fun confirmUserRegistration(registration: Registration)
}