package main.shoppilientmobile

import main.shoppilientmobile.domain.domainExposure.User

interface ServerApi {
    fun registerUser(user: User)
}