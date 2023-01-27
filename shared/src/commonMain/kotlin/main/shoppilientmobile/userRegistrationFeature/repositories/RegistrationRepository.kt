package main.shoppilientmobile.userRegistrationFeature.repositories

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

interface RegistrationRepository {
    suspend fun registerAdmin(registration: Registration): User
    suspend fun registerUser(registration: Registration)
    suspend fun confirmRegistration(registration: Registration): User
    suspend fun listenToRegistrations(): Flow<Registration>
}