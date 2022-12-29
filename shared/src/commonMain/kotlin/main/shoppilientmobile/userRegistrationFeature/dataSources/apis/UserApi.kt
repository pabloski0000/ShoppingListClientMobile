package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.jsonStructures.JsonStructure

interface UserApi {
    fun registerAdminUser(user: User): JsonStructure.SecurityToken
    fun registerUser(user: User)
}