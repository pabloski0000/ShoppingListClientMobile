package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.core.localStorage.KeyValueLocalStorage
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val keyValueLocalStorage: KeyValueLocalStorage,
): UserRepository {
    override suspend fun saveLocalUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalUser(): User {
        TODO("Not yet implemented")
    }
}