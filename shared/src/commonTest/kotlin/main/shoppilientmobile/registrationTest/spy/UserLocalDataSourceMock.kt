package main.shoppilientmobile.registrationTest.spy

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource

class UserLocalDataSourceMock : UserLocalDataSource {
    private lateinit var _user: User
    override suspend fun saveLocalUser(user: User) {
        _user = user
    }

    override suspend fun getLocalUser(): User {
        return _user
    }

    override suspend fun deleteLocalUser() {
        TODO("Not yet implemented")
    }
}