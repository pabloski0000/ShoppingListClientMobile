package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject
import main.shoppilientmobile.domain.domainExposure.User


interface UserApi {
    suspend fun subscribeAdmin(user: User): Flow<JsonObject>
    suspend fun registerBasicUser(user: User)
}

typealias SecurityToken = String
