package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApi
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException

class UserRemoteDataSourceImpl(
    private val userApi: UserApi
): UserRemoteDataSource {
    override fun registerUser(user: User) {
        when (user.getRole()) {
            UserRole.BASIC -> throwRemoteExceptionIfGoesWrong { userApi.registerUser(user) }
            UserRole.ADMIN -> throwRemoteExceptionIfGoesWrong { userApi.registerAdminUser(user) }
        }
    }

    private fun throwRemoteExceptionIfGoesWrong(task: () -> Unit) {
        try {
            task()
        } catch (e: Exception) {
            throw RemoteDataSourceException("Remote source unexpected behaviour")
        }
    }
}