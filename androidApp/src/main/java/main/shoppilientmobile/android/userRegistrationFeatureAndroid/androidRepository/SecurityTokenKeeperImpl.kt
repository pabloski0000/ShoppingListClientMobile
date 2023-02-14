package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.exceptions.NotFoundKeyException
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.core.storage.exceptions.ThereIsNoSecurityTokenException
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

class SecurityTokenKeeperImpl (
    private val keyValueLocalStorage: KeyValueLocalStorage,
): SecurityTokenKeeper {
    private var securityTokenKey: String = ""

    override suspend fun getSecurityToken(): SecurityToken {
        try {
            val securityToken = keyValueLocalStorage.getValue(securityTokenKey)
            return securityToken
        } catch (e: NotFoundKeyException) {
            throw ThereIsNoSecurityTokenException("Here there is no security token")
        }
    }

    override suspend fun setSecurityToken(securityToken: SecurityToken) {
        securityTokenKey = keyValueLocalStorage.store(securityToken)
    }
}