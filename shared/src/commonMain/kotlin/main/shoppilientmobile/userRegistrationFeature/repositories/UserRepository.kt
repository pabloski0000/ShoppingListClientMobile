package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

interface UserRepository {
    suspend fun saveLocalUser(user: User)
    suspend fun getLocalUser(): User
}