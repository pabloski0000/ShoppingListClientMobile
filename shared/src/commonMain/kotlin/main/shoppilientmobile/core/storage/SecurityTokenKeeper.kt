package main.shoppilientmobile.core.storage

import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

interface SecurityTokenKeeper {
    suspend fun getSecurityToken(): SecurityToken
    fun getSecurityToken2(): SecurityToken
    suspend fun setSecurityToken(securityToken: SecurityToken)
}