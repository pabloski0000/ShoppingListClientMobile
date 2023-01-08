package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.userRegistrationFeature.dataSources.NewUserRegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegistrationRepositoryImpl(
    private val newUserRegistrationRemoteDataSource: NewUserRegistrationRemoteDataSource,
): RegistrationRepository {

    override suspend fun confirmUserRegistration(registration: Registration) =
        newUserRegistrationRemoteDataSource.confirmUserRegistration(registration)
}