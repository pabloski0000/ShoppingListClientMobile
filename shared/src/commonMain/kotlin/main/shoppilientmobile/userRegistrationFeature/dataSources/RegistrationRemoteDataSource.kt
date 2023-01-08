package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

interface RegistrationRemoteDataSource {
    suspend fun registerAdmin(registration: Registration): User
    suspend fun registerUser(registration: Registration)
    suspend fun confirmUserRegistration(registration: Registration): User
}