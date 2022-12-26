package main.shoppilientmobile

import main.shoppilientmobile.domain.domainExposure.User

interface ApiServer {
    fun registerAdminUser(user: User)
    fun registerBasicUser(user: User)
}