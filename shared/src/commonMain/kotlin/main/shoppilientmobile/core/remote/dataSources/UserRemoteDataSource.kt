package main.shoppilientmobile.core.remote.dataSources

import main.shoppilientmobile.core.remote.UserApi

class UserRemoteDataSource(
    private val userApi: UserApi,
) {
    suspend fun deleteLocalUser() {
        userApi.deleteMyself()
    }
}