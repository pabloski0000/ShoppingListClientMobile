package main.shoppilientmobile.android

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.core.ObservableAppState
import main.shoppilientmobile.android.shoppingList.presentation.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.IntroduceCodeRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.FillNicknameViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.IntroduceCodeViewModelFactory
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase
import main.shoppilientmobile.shoppingList.application.SharedShoppingListObserver

class App(
    private val context: Context,
) {
    private val appRunner = AppRunner(context)
    private lateinit var androidContainer: AndroidContainer
    private lateinit var roleElectionViewModel: RoleElectionViewModel
    private lateinit var shoppingListViewModelFactory: ShoppingListViewModelFactory
    private lateinit var productFactoryViewModelFactory: ProductFactoryViewModelFactory
    private lateinit var registerAdminUseCase: RegisterAdminUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var userRoleRepository: UserRoleRepository
    private lateinit var registrationRepository: RegistrationRepository
    private lateinit var androidShoppingListUI: AndroidShoppingListUI
    private lateinit var remoteShoppingList: RemoteShoppingList
    private lateinit var observableAppState: ObservableAppState

    fun run(): AndroidContainer {
        androidContainer = appRunner.runApp()
        observableAppState = androidContainer.observableAppState
        val registrationContainer = androidContainer.registrationContainer!!
        roleElectionViewModel = RoleElectionViewModel(
            userRoleRepository = registrationContainer.userRoleRepository,
        )
        registerAdminUseCase = registrationContainer.registerAdminUseCase
        registerUserUseCase = registrationContainer.registerUserUseCase
        userRoleRepository = registrationContainer.userRoleRepository
        registrationRepository = androidContainer.registrationRepository
        androidShoppingListUI = androidContainer.androidShoppingListUI
        remoteShoppingList = androidContainer.remoteShoppingList
        return androidContainer
    }

    fun getFirstScreen(): @Composable () -> Unit {
        val startDestination = if (userIsAlreadyRegistered(androidContainer)) {
            SHOPPING_LIST_ROUTE
        } else {
            RoleElectionRoutableComposable.route
        }
        return {
            AppNavHost(startDestination, androidContainer)
        }
    }

    fun registerUserOnServerBlokingly(nickname: String, userRole: UserRole) {
        runBlocking {
            when (userRole) {
                UserRole.ADMIN -> registerAdminUseCase.registerAdmin(nickname)
                UserRole.BASIC -> registerUserUseCase.registerUser(nickname)
            }
        }
    }

    fun observeExternalShoppingList(observer: SharedShoppingListObserver) {
        remoteShoppingList.subscribe(observer)
    }

    fun onStart() {
        observableAppState.onStart()
    }

    fun onStop() {
        observableAppState.onStop()
    }

    private fun userIsAlreadyRegistered(androidContainer: AndroidContainer): Boolean {
        val user = runBlocking {
            androidContainer.getLocalUserUseCase.getLocalUser()
        }
        return user != null
    }

    @Composable
    private fun AppNavHost(startDestination: String, androidContainer: AndroidContainer) {
        val viewModelOwner = LocalViewModelStoreOwner.current!!
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(route = SHOPPING_LIST_ROUTE) {
                ShoppingListScreen(navController, androidContainer.shoppingListViewModelShared)
            }
            composable(route = PRODUCT_FACTORY_ROUTE) {
                ProductFactoryScreen(navController, androidContainer.shoppingListViewModelShared)
            }
            composable(route = RoleElectionRoutableComposable.route) {
                RoleElectionRoutableComposable.RoleElection(
                    viewModel = roleElectionViewModel,
                    navController,
                )
            }
            composable(route = FillNicknameRoutableComposable.route) {
                val viewModel = viewModel<FillNicknameViewModel>(
                    factory = FillNicknameViewModelFactory(
                        registerAdminUseCase,
                        registerUserUseCase,
                        userRoleRepository,
                        navController,
                    )
                )
                FillNicknameRoutableComposable.FillNickname(viewModel)
            }
            val introduceCodePathScreenPath = "userNickname"
            composable(route = "${IntroduceCodeRoutableComposable.route}/{$introduceCodePathScreenPath}") { entry ->
                val viewModel = viewModel<IntroduceCodeViewModel>(
                    factory = IntroduceCodeViewModelFactory(
                        registrationRepository,
                        androidContainer.userRepository,
                        navController,
                        androidContainer.registrationContainer!!.confirmUserRegistrationUseCase,
                    )
                )
                val userNickname = entry.arguments!!.getString(introduceCodePathScreenPath)!!
                IntroduceCodeRoutableComposable.IntroduceCode(
                    viewModel = viewModel,
                    userNickname = userNickname,
                )
            }
            composable(SHOPPING_MODE_SCREEN_ROUTE) {
                ShoppingModeScreen(
                    viewModel = androidContainer.shoppingModeViewModel,
                    navController = navController,
                )
            }
            composable(USER_REGISTRATIONS_SCREEN_ROUTE) {
                UserRegistrationsScreen(
                    androidContainer.userRegistrationsViewModel,
                )
            }
        }
    }
}