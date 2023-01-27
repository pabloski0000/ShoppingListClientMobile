package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource

class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun saveLocalUser(user: User) {
        userLocalDataSource.save(user)
    }

    override suspend fun getLocalUser(): User {
        return userLocalDataSource.getUser()
    }
}