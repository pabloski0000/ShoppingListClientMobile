package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User

interface UserLocalDataSource {
    suspend fun saveLocalUser(user: User)
    suspend fun getLocalUser(): User
    suspend fun deleteLocalUser()
}