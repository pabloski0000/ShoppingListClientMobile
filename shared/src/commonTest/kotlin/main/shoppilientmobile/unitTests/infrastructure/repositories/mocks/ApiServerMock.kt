package main.shoppilientmobile.unitTests.infrastructure.repositories.mocks

import main.shoppilientmobile.ApiServer
import main.shoppilientmobile.domain.domainExposure.User

class ApiServerMock: ApiServer {
    var registerAdminUserMethodCalled = false
    var registerBasicUserMethodCalled = false

    override fun registerAdminUser(user: User) {
        registerAdminUserMethodCalled = true
    }

    override fun registerBasicUser(user: User) {
        registerBasicUserMethodCalled = true
    }

}