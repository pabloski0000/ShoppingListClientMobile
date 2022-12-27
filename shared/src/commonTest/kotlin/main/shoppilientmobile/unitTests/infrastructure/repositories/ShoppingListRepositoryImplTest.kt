package main.shoppilientmobile.unitTests.infrastructure.repositories

import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.repositories.SharedShoppingListServerImpl
import main.shoppilientmobile.unitTests.infrastructure.repositories.mocks.ApiServerMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ShoppingListRepositoryImplTest {
    private lateinit var userRepositoryImpl: SharedShoppingListServerImpl
    private lateinit var apiServerMock: ApiServerMock
    @BeforeTest
    fun setUp() {
        apiServerMock = ApiServerMock()
        userRepositoryImpl = SharedShoppingListServerImpl(apiServerMock)
    }

    @Test
    fun assertItAddsAdminCorrectly() {
        userRepositoryImpl.registerUser(UserBuilderImpl().setRole(User.Role.ADMIN).build())
        assertTrue {
            apiServerMock.registerAdminUserMethodCalled
        }
    }

    @Test
    fun assertItAddsBasicUserCorrectly() {
        userRepositoryImpl.registerUser(UserBuilderImpl().build())
        assertTrue {
            apiServerMock.registerBasicUserMethodCalled
        }
    }
}