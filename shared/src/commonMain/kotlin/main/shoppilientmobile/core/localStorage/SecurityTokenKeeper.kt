package main.shoppilientmobile.core.localStorage

import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

interface SecurityTokenKeeper {
    suspend fun getSecurityToken(): SecurityToken
    suspend fun setSecurityToken(securityToken: SecurityToken)
}