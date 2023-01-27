package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User

interface UserLocalDataSource {
    suspend fun save(user: User)
    suspend fun getUser(): User
}