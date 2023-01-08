package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

interface UserRepository {
    suspend fun registerAdmin(user: User): AdminRegistration
    suspend fun registerUser(user: User)
}