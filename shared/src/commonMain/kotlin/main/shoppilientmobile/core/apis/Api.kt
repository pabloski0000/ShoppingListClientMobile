package main.shoppilientmobile.core.apis

import main.shoppilientmobile.core.localStorage.SecurityTokenKeeper
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

open class Api(
    private val securityTokenKeeper: SecurityTokenKeeper,
) {
    protected suspend fun keepSecurityToken(securityToken: SecurityToken) {
        securityTokenKeeper.setSecurityToken(securityToken)
    }

    protected suspend fun getSecurityToken(): SecurityToken {
        return securityTokenKeeper.getSecurityToken()
    }
}