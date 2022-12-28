package main.shoppilientmobile.unitTests.infrastructure.repositories.mocks

import main.shoppilientmobile.ServerApi
import main.shoppilientmobile.domain.domainExposure.User

class ApiServerMock: ServerApi {
    private val registeredUsers = mutableListOf<User>()
    var registerAdminUserMethodCalled = false
    var registerBasicUserMethodCalled = false

    fun registerAdminUser(user: User) {
        registerAdminUserMethodCalled = true
    }

    fun registerBasicUser(user: User) {
        registerBasicUserMethodCalled = true
    }

    override fun registerUser(user: User) {
        registeredUsers.add(user)
    }

}