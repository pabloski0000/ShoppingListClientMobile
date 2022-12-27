package main.shoppilientmobile.unitTests.application

import main.shoppilientmobile.application.RegisterUserUseCaseImpl
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.UserRole
import main.shoppilientmobile.unitTests.application.mocks.ShoppingListMock
import main.shoppilientmobile.unitTests.application.mocks.ShoppingListRepositoryMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class RegisterUserUseCaseImplTest {
    private lateinit var shoppingListMock: ShoppingListMock
    private lateinit var shoppingListRepositoryMock: ShoppingListRepositoryMock
    private lateinit var registerUserUseCase: RegisterUserUseCaseImpl

    @BeforeTest
    fun setUp() {
        shoppingListMock = ShoppingListMock()
        shoppingListRepositoryMock = ShoppingListRepositoryMock()
        registerUserUseCase = RegisterUserUseCaseImpl(
            shoppingListMock,
            shoppingListRepositoryMock,
            UserBuilderImpl(),
        )
    }

    @Test
    fun assertItRegistersUserCorrectly() {
        registerUserUseCase.registerUser(UserRegistrationData("pa", UserRole.ADMIN))
        assertTrue {
            shoppingListMock.getRegisteredUsers().size == 1 &&
                    shoppingListRepositoryMock.getRegisteredUsers().size == 1
        }
    }
}