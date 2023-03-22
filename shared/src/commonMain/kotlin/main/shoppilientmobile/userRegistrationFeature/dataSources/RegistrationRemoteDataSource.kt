package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.shoppingList.application.UserRegistrationsListener
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.useCases.exceptions.WrongCodeException
import kotlin.coroutines.cancellation.CancellationException

interface RegistrationRemoteDataSource {
    suspend fun registerAdmin(registration: Registration): User
    suspend fun registerUser(registration: Registration)
    @Throws(CancellationException::class, WrongCodeException::class)
    suspend fun confirmUserRegistration(registration: Registration): User
    suspend fun listenToUserRegistrations(userRegistrationListener: UserRegistrationsListener)
}