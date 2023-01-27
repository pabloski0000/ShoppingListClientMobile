package main.shoppilientmobile.registrationTest.spy

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource

class UserLocalDataSourceMock : UserLocalDataSource {
    private lateinit var _user: User
    override suspend fun save(user: User) {
        _user = user
    }

    override suspend fun getUser(): User {
        return _user
    }
}