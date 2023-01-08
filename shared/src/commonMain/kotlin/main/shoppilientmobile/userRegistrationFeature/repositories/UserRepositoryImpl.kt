package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {

    override suspend fun registerAdmin(user: User) = userRemoteDataSource.registerAdmin(user)

    override suspend fun registerUser(user: User) = userRemoteDataSource.registerUser(user)
}