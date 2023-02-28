package main.shoppilientmobile.core.remote.dataSources

import main.shoppilientmobile.core.remote.AsynchronousHttpClientImpl
import main.shoppilientmobile.core.remote.UserApi

class UserRemoteDataSource(
    private val userApi: UserApi,
) {
    fun deleteLocalUser() {
        userApi.deleteMyself()
    }
}