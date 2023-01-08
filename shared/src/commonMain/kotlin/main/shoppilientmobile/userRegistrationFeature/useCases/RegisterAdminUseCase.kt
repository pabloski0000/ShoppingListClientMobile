package main.shoppilientmobile.userRegistrationFeature.useCases

import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

interface RegisterAdminUseCase {
    suspend fun registerAdmin(nickname: String): AdminRegistration
}