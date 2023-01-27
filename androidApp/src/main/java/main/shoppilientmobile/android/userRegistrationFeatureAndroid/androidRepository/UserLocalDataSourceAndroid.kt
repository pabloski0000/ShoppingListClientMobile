package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource

class UserLocalDataSourceAndroid : UserLocalDataSource {
    override suspend fun save(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }
}