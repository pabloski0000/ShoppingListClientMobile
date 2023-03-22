package main.shoppilientmobile.android.infraestructure.doubles

import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.SecurityToken

class SecurityTokenKeeperMock : SecurityTokenKeeper {
    var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    override suspend fun getSecurityToken(): SecurityToken {
        TODO("Not yet implemented")
    }

    override fun getSecurityToken2(): SecurityToken {
        TODO("Not yet implemented")
    }

    override suspend fun setSecurityToken(securityToken: SecurityToken) {
        TODO("Not yet implemented")
    }
}