package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.core.localStorage.SecurityTokenKeeper
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApi
import main.shoppilientmobile.userRegistrationFeature.dataSources.exceptions.RemoteDataSourceException

class UserRemoteDataSourceImpl(
    private val userApi: UserApi,
    private val securityTokenKeeper: SecurityTokenKeeper,
): UserRemoteDataSource {

    override suspend fun registerUser(user: User) {
        when (user.getRole()) {
            UserRole.BASIC -> throwRemoteExceptionIfGoesWrong { userApi.registerUser(user) }
            UserRole.ADMIN -> throwRemoteExceptionIfGoesWrong {
                saveSecurityToken(userApi.registerAdminUser(user))
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

    private suspend fun saveSecurityToken(securityToken: SecurityToken) {
        securityTokenKeeper.setSecurityToken(securityToken)
    }
}