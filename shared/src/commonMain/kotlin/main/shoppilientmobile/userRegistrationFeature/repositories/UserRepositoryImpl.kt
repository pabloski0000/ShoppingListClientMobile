package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserRemoteDataSource

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override fun registerUser(user: User) {
        userRemoteDataSource.registerUser(user)
    }
}