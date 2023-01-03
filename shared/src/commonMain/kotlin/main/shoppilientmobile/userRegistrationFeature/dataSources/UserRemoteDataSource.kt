package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User

interface UserRemoteDataSource {
    suspend fun registerUser(user: User)
}