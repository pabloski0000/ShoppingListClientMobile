package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User

interface UserRemoteDataSource {
    fun registerUser(user: User)
}