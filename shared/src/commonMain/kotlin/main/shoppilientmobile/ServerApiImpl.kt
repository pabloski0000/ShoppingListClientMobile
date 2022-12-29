package main.shoppilientmobile

import main.shoppilientmobile.domain.domainExposure.User

class ServerApiImpl(

): ServerApi {

    override fun registerUser(user: User) {
        /*when (user.getRole()) {
            UserRole.BASIC -> serverRequests.registerBasicUser(user)
            UserRole.ADMIN -> serverRequests.registerAdminUser(user)
        }*/
    }
}