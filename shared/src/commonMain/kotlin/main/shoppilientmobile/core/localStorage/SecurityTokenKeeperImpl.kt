package main.shoppilientmobile.core.localStorage

import main.shoppilientmobile.core.localStorage.exceptions.NotFoundKeyException
import main.shoppilientmobile.core.localStorage.exceptions.ThereIsNoSecurityTokenException
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

class SecurityTokenKeeperImpl (
    private val keyValueLocalStorage: KeyValueLocalStorage,
): SecurityTokenKeeper {
    private val securityTokenKey = "35asd4f3a2sd1f3dsa4f3sasad5312sd4a35fasd"

    override suspend fun getSecurityToken(): SecurityToken {
        try {
            return keyValueLocalStorage.getValue(securityTokenKey)
        } catch (e: NotFoundKeyException) {
            throw ThereIsNoSecurityTokenException("Here there is no security token")
        }
    }

    override suspend fun setSecurityToken(securityToken: SecurityToken) {
        keyValueLocalStorage.put(securityTokenKey, securityToken)
    }
}