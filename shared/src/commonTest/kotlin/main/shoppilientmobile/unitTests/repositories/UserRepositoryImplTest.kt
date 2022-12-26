package main.shoppilientmobile.unitTests.repositories

import main.shoppilientmobile.ApiServer
import main.shoppilientmobile.domain.Role
import main.shoppilientmobile.domain.User
import main.shoppilientmobile.repositories.UserRepositoryImpl
import main.shoppilientmobile.unitTests.repositories.mocks.ApiServerMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class UserRepositoryImplTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var apiServerMock: ApiServerMock
    @BeforeTest
    fun setUp() {
        apiServerMock = ApiServerMock()
        userRepositoryImpl = UserRepositoryImpl(apiServerMock)
    }

    @Test
    fun assertItAddsAdminCorrectly() {
        userRepositoryImpl.addUser(User("pabloski", Role.ADMIN))
        assertTrue {
            apiServerMock.registerAdminUserMethodCalled
        }
    }

    @Test
    fun assertItAddsBasicUserCorrectly() {
        userRepositoryImpl.addUser(User("pabloski", Role.BASIC))
        assertTrue {
            apiServerMock.registerBasicUserMethodCalled
        }
    }
}