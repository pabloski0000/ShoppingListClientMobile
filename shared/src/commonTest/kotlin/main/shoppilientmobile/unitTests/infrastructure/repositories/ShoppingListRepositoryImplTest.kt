package main.shoppilientmobile.unitTests.infrastructure.repositories

import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.repositories.SharedShoppingListServerImpl
import main.shoppilientmobile.unitTests.infrastructure.repositories.mocks.ApiServerMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ExternalSharedShoppingListImplTest {
    private lateinit var userRepositoryImpl: SharedShoppingListServerImpl
    private lateinit var apiServerMock: ApiServerMock
    @BeforeTest
    fun setUp() {
        apiServerMock = ApiServerMock()
        userRepositoryImpl = SharedShoppingListServerImpl(apiServerMock)
    }

    /*@Test
    fun assertItAddsAdminCorrectly() {
        userRepositoryImpl.registerUser(UserBuilderImpl().setRole(UserRole.ADMIN).build())
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
    }*/
}