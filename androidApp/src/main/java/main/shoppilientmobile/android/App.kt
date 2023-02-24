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
import main.shoppilientmobile.android.shoppingList.presentation.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.IntroduceCodeRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.FillNicknameViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.IntroduceCodeViewModelFactory
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase
import main.shoppilientmobile.shoppingList.application.ShoppingListObserver

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

    fun run(): AndroidContainer {
        androidContainer = appRunner.runApp()
        val registrationContainer = androidContainer.registrationContainer!!
        roleElectionViewModel = RoleElectionViewModel(
            userRoleRepository = registrationContainer.userRoleRepository,
        )
        registerAdminUseCase = registrationContainer.registerAdminUseCase
        registerUserUseCase = registrationContainer.registerUserUseCase
        userRoleRepository = registrationContainer.userRoleRepository
        registrationRepository = registrationContainer.registrationRepository
        shoppingListViewModelFactory = androidContainer.shoppingListViewModelFactory
        productFactoryViewModelFactory = androidContainer.productFactoryViewModelFactory
        androidShoppingListUI = androidContainer.androidShoppingListUI
        remoteShoppingList = androidContainer.remoteShoppingList
        return androidContainer
    }

    fun getFirstScreen(): @Composable () -> Unit {
        val startDestination = if (userIsAlreadyRegistered(androidContainer)) {
            SHOPPING_LIST_ROUTE2
        } else {
            RoleElectionRoutableComposable.route
        }
        return {
            AppNavHost(startDestination)
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

    fun observeExternalShoppingList(observer: ShoppingListObserver) {
        remoteShoppingList.observe(observer)
    }

    private fun userIsAlreadyRegistered(androidContainer: AndroidContainer): Boolean {
        val user = runBlocking {
            androidContainer.getUserUseCase.getUser()
        }
        return user != null
    }

    @Composable
    private fun AppNavHost(startDestination: String) {
        val viewModelOwner = LocalViewModelStoreOwner.current!!
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(route = SHOPPING_LIST_ROUTE2) {
                val viewModel = viewModel<ShoppingListViewModel2>(
                    viewModelOwner,
                    "FirstViewModelInGraph",
                    shoppingListViewModelFactory,
                )
                ShoppingListScreen2(
                    navController = navController,
                    viewModel = viewModel,
                )
            }
            composable(route = PRODUCT_FACTORY_ROUTE2) {
                val viewModel = viewModel<ProductFactoryViewModel2>(
                    viewModelOwner,
                    "SecondViewModelInGraph",
                    productFactoryViewModelFactory,
                )
                ProductFactoryScreen2(navController, viewModel)
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
                    factory = IntroduceCodeViewModelFactory(registrationRepository, navController)
                )
                val userNickname = entry.arguments!!.getString(introduceCodePathScreenPath)!!
                IntroduceCodeRoutableComposable.IntroduceCode(
                    viewModel = viewModel,
                    userNickname = userNickname,
                )
            }
        }
    }
}