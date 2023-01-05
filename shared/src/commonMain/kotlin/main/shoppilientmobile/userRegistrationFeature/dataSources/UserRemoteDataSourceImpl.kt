package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApi
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException

class UserRemoteDataSourceImpl(
    private val userApi: UserApi,
): UserRemoteDataSource {

    override suspend fun registerUser(user: User) {
        when (user.getRole()) {
            UserRole.BASIC -> throwRemoteExceptionIfGoesWrong { userApi.registerBasicUser(user) }
            UserRole.ADMIN -> throwRemoteExceptionIfGoesWrong {
                userApi.subscribeAdmin(user)
            }
        }
    }

    private suspend fun throwRemoteExceptionIfGoesWrong(remoteTask: suspend () -> Unit) {
        try {
            remoteTask()
        } catch (e: Exception) {
            throw RemoteDataSourceException("Remote source unexpected behaviour")
        }
    }
}