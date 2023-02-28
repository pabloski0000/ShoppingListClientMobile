package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.core.remote.dataSources.UserRemoteDataSource
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun saveLocalUser(user: User) {
        userLocalDataSource.saveLocalUser(user)
    }

    override suspend fun getLocalUser(): User {
        return userLocalDataSource.getLocalUser()
    }

    override suspend fun deleteLocalUser() {
        userRemoteDataSource.deleteLocalUser()
        userLocalDataSource.deleteLocalUser()
    }
}