package main.shoppilientmobile.repositories

import main.shoppilientmobile.ServerApi
import main.shoppilientmobile.domain.domainExposure.User

class SharedShoppingListServerImpl(
    private val serverApi: ServerApi
) {

    fun registerUser(user: User) {
        serverApi.registerUser(user)
    }
}