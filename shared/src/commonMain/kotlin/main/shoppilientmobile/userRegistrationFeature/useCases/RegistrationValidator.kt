package main.shoppilientmobile.userRegistrationFeature.useCases

interface RegistrationValidator {
    suspend fun confirmRegistration(code: String): Boolean
}
