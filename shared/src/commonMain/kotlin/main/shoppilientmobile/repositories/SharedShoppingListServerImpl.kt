package main.shoppilientmobile.repositories

import main.shoppilientmobile.ApiServer
import main.shoppilientmobile.domain.domainExposure.User

class SharedShoppingListServerImpl(
    private val apiServer: ApiServer
) {

    fun registerUser(user: User) {
        when(user.getRole()) {
            User.Role.BASIC -> apiServer.registerBasicUser(user)
            User.Role.ADMIN -> apiServer.registerAdminUser(user)
        }
    }
}