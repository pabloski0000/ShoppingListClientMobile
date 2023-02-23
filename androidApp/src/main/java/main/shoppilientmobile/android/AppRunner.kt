package main.shoppilientmobile.android

import android.content.Context
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.shoppingList.presentation.ProductFactoryViewModelFactory
import main.shoppilientmobile.android.shoppingList.presentation.ShoppingListViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class AppRunner(
    private val context: Context,
) {
    private lateinit var roleElectionViewModel: RoleElectionViewModel
    private lateinit var shoppingListViewModelFactory: ShoppingListViewModelFactory
    private lateinit var productFactoryViewModelFactory: ProductFactoryViewModelFactory
    private lateinit var registerAdminUseCase: RegisterAdminUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var userRoleRepository: UserRoleRepository
    private lateinit var registrationRepository: RegistrationRepository

    fun runApp(): AndroidContainer {
        val androidContainer = AndroidContainer(context)
        val httpClient = androidContainer.httpClient
        val userRepository = androidContainer.userRepository
        val securityTokenKeeper = androidContainer.securityTokenKeeper
        val shoppingListSynchroniserUseCase = androidContainer.shoppingListSynchroniserUseCase
        val registrationContainer = RegistrationContainer(
            httpClient = httpClient,
            securityTokenKeeper = securityTokenKeeper,
            userRepository = userRepository,
            userRoleLocalDataSource = androidContainer.room.userRoleDao(),
            shoppingListSynchroniserUseCase,
        )
        androidContainer.registrationContainer = registrationContainer
        roleElectionViewModel = RoleElectionViewModel(
            userRoleRepository = registrationContainer.userRoleRepository,
        )
        registerAdminUseCase = registrationContainer.registerAdminUseCase
        registerUserUseCase = registrationContainer.registerUserUseCase
        userRoleRepository = registrationContainer.userRoleRepository
        registrationRepository = registrationContainer.registrationRepository
        shoppingListViewModelFactory = androidContainer.shoppingListViewModelFactory
        productFactoryViewModelFactory = androidContainer.productFactoryViewModelFactory

        return androidContainer
    }
}