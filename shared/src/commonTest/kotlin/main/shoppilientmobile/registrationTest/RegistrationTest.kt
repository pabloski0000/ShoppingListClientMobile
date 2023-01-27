package main.shoppilientmobile.registrationTest

import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.registrationTest.spy.RegistrationRemoteDataSourceMock
import main.shoppilientmobile.registrationTest.spy.UserLocalDataSourceMock
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCaseImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RegistrationTest {
    private lateinit var registerAdminUseCase: RegisterAdminUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var registrationRemoteDataSourceMock: RegistrationRemoteDataSourceMock
    private lateinit var userLocalDataSourceMock: UserLocalDataSourceMock

    @BeforeTest
    fun setUp() {
        registrationRemoteDataSourceMock = RegistrationRemoteDataSourceMock()
        userLocalDataSourceMock = UserLocalDataSourceMock()
        val registrationRepository = RegistrationRepositoryImpl(
            registrationRemoteDataSourceMock,
        )
        val userRepository = UserRepositoryImpl(
            userLocalDataSourceMock,
        )
        registerAdminUseCase = RegisterAdminUseCaseImpl(
            registrationRepository,
            userRepository,
        )
        registerUserUseCase = RegisterUserUseCase(
            registrationRepository,
            userRepository,
        )
    }

    @Test
    fun registerAdmin() {
        val userNickname = "pabloski"
        val expectedUser = UserBuilderImpl().giveItANickname(userNickname).setRole(UserRole.ADMIN).build()
        runBlocking {
            registerAdminUseCase.registerAdmin(userNickname)
            assertEquals(
                expectedUser,
                userLocalDataSourceMock.getUser()
            )
        }
    }

    @Test
    fun registerUser() {
        val userNickname = "randomNickname"
        val expectedUser = UserBuilderImpl().giveItANickname(userNickname).setRole(UserRole.BASIC).build()
        val registrationSignature = "12345"
        runBlocking {
            val confirmRegistration = registerUserUseCase.registerUser(userNickname)
            confirmRegistration.confirmRegistration(registrationSignature)
            assertEquals(
                expectedUser,
                userLocalDataSourceMock.getUser(),
            )
        }
    }

    @Test
    fun registerUserWithWrongCode() {
        val userNickname = "randomNickname"
        val expectedUser = UserBuilderImpl().giveItANickname(userNickname).setRole(UserRole.BASIC).build()
        val registrationSignature = "wrongCode"
        runBlocking {
            val confirmRegistration = registerUserUseCase.registerUser(userNickname)
            confirmRegistration.confirmRegistration(registrationSignature)
            assertEquals(
                expectedUser,
                userLocalDataSourceMock.getUser(),
            )
        }
    }
}