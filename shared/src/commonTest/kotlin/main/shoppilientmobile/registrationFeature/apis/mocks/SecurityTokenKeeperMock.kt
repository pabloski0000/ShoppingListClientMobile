package main.shoppilientmobile.registrationFeature.apis.mocks

import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

class SecurityTokenKeeperMock: SecurityTokenKeeper {
    private var securityTokenMock = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQYWJsbyIsImV4cCI6MTY3MzEzODA3MywiYXV0aG9yaXRpZXMiOlsiQURNSU4iXX0.zUYtbT1_E8lWoHJP3rCLNtg6Fap8Dots-e25VQR4ZEc"
    override suspend fun getSecurityToken() = securityTokenMock

    override suspend fun setSecurityToken(securityToken: SecurityToken) {
        securityTokenMock = securityToken
    }
}