package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.shoppingList.presentation.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.RegistrationFeatureNavHost
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.IntroduceCodeRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.FillNicknameViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.IntroduceCodeViewModelFactory
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class MainActivity : ComponentActivity() {
    private lateinit var androidContainer: AndroidContainer
    private lateinit var roleElectionViewModel: RoleElectionViewModel
    private lateinit var shoppingListViewModelFactory: ShoppingListViewModelFactory
    private lateinit var productFactoryViewModelFactory: ProductFactoryViewModelFactory
    private lateinit var registerAdminUseCase: RegisterAdminUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var userRoleRepository: UserRoleRepository
    private lateinit var registrationRepository: RegistrationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startUp()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    MainActivityScreen()
                }
            }
        }
    }

    private fun startUp() {
        val androidApplication = (application as AndroidApplication)
        androidApplication.androidContainer = AndroidContainer(this)
        androidContainer = androidApplication.androidContainer!!
        val httpClient = androidContainer.httpClient
        val userRepository = androidContainer.userRepository
        val securityTokenKeeper = androidContainer.securityTokenKeeper
        val registrationContainer = RegistrationContainer(
            httpClient = httpClient,
            securityTokenKeeper = securityTokenKeeper,
            userRepository = userRepository,
            userRoleLocalDataSource = androidContainer.room.userRoleDao(),
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
    }

    @Preview(showSystemUi = true)
    @Composable
    fun MainActivityScreen() {
        val viewModelOwner = LocalViewModelStoreOwner.current!!
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = RoleElectionRoutableComposable.route,
        ) {
            composable(route = SHOPPING_LIST_ROUTE) {
                val viewModel = viewModel<ShoppingListViewModel>(
                    viewModelOwner,
                    "FirstViewModelInGraph",
                    shoppingListViewModelFactory,
                )
                ShoppingListScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(route = PRODUCT_FACTORY_ROUTE) {
                val viewModel = viewModel<ProductFactoryViewModel>(
                    viewModelOwner,
                    "SecondViewModelInGraph",
                    productFactoryViewModelFactory,
                )
                ProductFactoryScreen(navController, viewModel)
            }
            composable(route = RoleElectionRoutableComposable.route) {
                RoleElectionRoutableComposable.RoleElection(
                    viewModel = roleElectionViewModel,
                    navController,
                )
            }
            val fillNicknameScreenPath = "{userRole}"
            composable(route = "${FillNicknameRoutableComposable.route}/$fillNicknameScreenPath") { entry ->
                val userRole = adaptToUserRole(
                    entry.arguments!!.getString(fillNicknameScreenPath
                        .filter { it != '{' && it != '}' })!!
                )
                val viewModel = viewModel<FillNicknameViewModel>(
                    factory = FillNicknameViewModelFactory(
                        registerAdminUseCase,
                        registerUserUseCase,
                        userRoleRepository,
                        navController,
                    )
                )
                FillNicknameRoutableComposable.FillNickname(
                    viewModel = viewModel,
                    navController,
                    userRole,
                )
            }
            val introduceCodePathScreenPath = "userNickname"
            composable(route = "${IntroduceCodeRoutableComposable.route}/{$introduceCodePathScreenPath}") { entry ->
                val viewModel = viewModel<IntroduceCodeViewModel>(
                    factory = IntroduceCodeViewModelFactory(registrationRepository)
                )
                val userNickname = entry.arguments!!.getString(introduceCodePathScreenPath)!!
                IntroduceCodeRoutableComposable.IntroduceCode(
                    viewModel = viewModel,
                    userNickname = userNickname,
                )
            }
        }
    }

    private fun adaptToUserRole(userRole: String): UserRole {
        return when {
            userRole.uppercase() == "ADMIN" -> UserRole.ADMIN
            userRole.uppercase() == "BASIC" -> UserRole.BASIC
            else -> throw IllegalArgumentException("Unexpected user role argument")
        }
    }
}
