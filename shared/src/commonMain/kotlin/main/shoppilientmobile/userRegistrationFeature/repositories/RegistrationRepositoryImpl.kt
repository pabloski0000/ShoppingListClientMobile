package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.userRegistrationFeature.dataSources.RegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegistrationRepositoryImpl(
    private val registrationRemoteDataSource: RegistrationRemoteDataSource,
): RegistrationRepository {
    override suspend fun registerAdmin(registration: Registration) =
        registrationRemoteDataSource.registerAdmin(registration)

    override suspend fun registerUser(registration: Registration) =
        registrationRemoteDataSource.registerUser(registration)

    override suspend fun validateRegistration(registration: Registration) =
        registrationRemoteDataSource.confirmUserRegistration(registration)
}