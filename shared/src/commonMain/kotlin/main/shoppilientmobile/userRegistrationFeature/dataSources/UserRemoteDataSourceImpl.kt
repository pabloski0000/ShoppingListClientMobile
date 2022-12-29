package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApi

class UserRemoteDataSourceImpl(
    private val userApi: UserApi
): UserRemoteDataSource {
    override suspend fun registerUser(user: User) {
        when (user.getRole()) {
            UserRole.BASIC -> userApi.registerUser(user)
            UserRole.ADMIN -> userApi.registerAdminUser(user)
        }
    }
}