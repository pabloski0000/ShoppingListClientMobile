package main.shoppilientmobile.userRegistrationFeature.repositories

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.shoppingList.application.UserRegistrationsListener
import main.shoppilientmobile.userRegistrationFeature.dataSources.RegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegistrationRepositoryImpl(
    private val registrationRemoteDataSource: RegistrationRemoteDataSource,
): RegistrationRepository {
    override suspend fun registerAdmin(registration: Registration) =
        registrationRemoteDataSource.registerAdmin(registration)

    override suspend fun registerUser(registration: Registration) =
        registrationRemoteDataSource.registerUser(registration)

    override suspend fun confirmRegistration(registration: Registration) =
        registrationRemoteDataSource.confirmUserRegistration(registration)

    override suspend fun listenToUserRegistrations(userRegistrationsListener: UserRegistrationsListener) {
        registrationRemoteDataSource.listenToUserRegistrations(userRegistrationsListener)
    }

    override suspend fun listenToRegistrations(): Flow<Registration> {
        TODO("Not yet implemented")
    }
}