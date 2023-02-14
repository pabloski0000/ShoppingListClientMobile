package main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers

import main.shoppilientmobile.core.remote.AsynchronousHttpClientImpl
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.shoppingList.application.ShoppingListSynchroniserUseCase
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.RegistrationApi
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleLocalDataSource
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCaseImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class RegistrationContainer(
    httpClient: AsynchronousHttpClientImpl,
    securityTokenKeeper: SecurityTokenKeeper,
    userRepository: UserRepository,
    userRoleLocalDataSource: UserRoleLocalDataSource,
    shoppingListSynchroniserUseCase: ShoppingListSynchroniserUseCase,
) {

    private val registrationApi = RegistrationApi(
        httpClient = httpClient,
        securityTokenKeeper = securityTokenKeeper,
    )

    val registrationRepository = RegistrationRepositoryImpl(
        registrationRemoteDataSource = registrationApi,
    )

    val userRoleRepository = UserRoleRepository(
        userRoleLocalDataSource = userRoleLocalDataSource,
    )

    val registerUserUseCase = RegisterUserUseCase(
        registrationRepository,
        userRepository,
    )

    val registerAdminUseCase = RegisterAdminUseCaseImpl(
        registrationRepository = registrationRepository,
        userRepository = userRepository,
        shoppingListSynchroniserUseCase,
    )
}