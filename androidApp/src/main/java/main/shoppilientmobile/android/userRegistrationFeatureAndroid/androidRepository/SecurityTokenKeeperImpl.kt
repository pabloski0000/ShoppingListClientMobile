package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.exceptions.NotFoundKeyException
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.core.storage.exceptions.ThereIsNoSecurityTokenException
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

class SecurityTokenKeeperImpl (
    private val keyValueLocalStorage: KeyValueLocalStorage,
): SecurityTokenKeeper {
    private val securityTokenKey: String = "security_token_key"

    override suspend fun getSecurityToken(): SecurityToken {
        val securityToken = keyValueLocalStorage.getValue(securityTokenKey)
        return securityToken
    }
    override fun getSecurityToken2(): SecurityToken {
        return try {
            runBlocking(Dispatchers.IO) {
                val securityToken = keyValueLocalStorage.getValue(securityTokenKey)
                return@runBlocking securityToken
            }
        } catch (e: NotFoundKeyException) {
            throw ThereIsNoSecurityTokenException("Here there is no security token")
        }
    }

    override suspend fun setSecurityToken(securityToken: SecurityToken) {
        keyValueLocalStorage.store(securityTokenKey, securityToken)
    }
}