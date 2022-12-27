package main.shoppilientmobile.unitTests.application

import main.shoppilientmobile.application.RegisterUserUseCaseImpl
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.UserRole
import main.shoppilientmobile.unitTests.application.mocks.SharedShoppingListMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class RegisterUserUseCaseTest {
    private lateinit var shoppingListMock: SharedShoppingListMock
    private lateinit var registerUserUseCase: RegisterUserUseCaseImpl

    @BeforeTest
    fun setUp() {
        shoppingListMock = SharedShoppingListMock()
        registerUserUseCase = RegisterUserUseCaseImpl(
            shoppingListMock,
            UserBuilderImpl()
        )
    }

    @Test
    fun assertItRegistersUserCorrectly() {
        registerUserUseCase.registerUser(UserRegistrationData("pa", UserRole.ADMIN))
        assertTrue {
            shoppingListMock.getRegisteredUsers().size == 1
        }
    }
}