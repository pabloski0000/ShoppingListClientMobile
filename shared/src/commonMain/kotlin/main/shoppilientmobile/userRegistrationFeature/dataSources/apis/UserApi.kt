package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import main.shoppilientmobile.domain.domainExposure.User


interface UserApi {
    fun registerAdminUser(user: User): SecurityToken
    fun registerUser(user: User)
}

typealias SecurityToken = String
