package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.shoppingList.presentation.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel

class MainActivity : ComponentActivity() {
    private lateinit var roleElectionViewModel: RoleElectionViewModel
    private lateinit var fillNicknameViewModel: FillNicknameViewModel
    private lateinit var shoppingListViewModelFactory: ShoppingListViewModelFactory
    private lateinit var shoppingListDeletionViewModelFactory: ShoppingListDeletionViewModelFactory
    private lateinit var shoppingListNormalViewModelFactory: ShoppingListNormalViewModelFactory
    private lateinit var productFactoryViewModelFactory: ProductFactoryViewModelFactory

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
        val androidContainer = androidApplication.androidContainer!!
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
        fillNicknameViewModel = FillNicknameViewModel(
            userRoleRepository = registrationContainer.userRoleRepository,
            registerAdminUseCase = registrationContainer.registerAdminUseCase,
            registerUserUseCase = registrationContainer.registerUserUseCase,
        )
        shoppingListViewModelFactory = ShoppingListViewModelFactory(androidContainer.shoppingList)
        shoppingListDeletionViewModelFactory =
            ShoppingListDeletionViewModelFactory(androidContainer.shoppingList)
        shoppingListNormalViewModelFactory =
            ShoppingListNormalViewModelFactory(androidContainer.shoppingList)
        productFactoryViewModelFactory = ProductFactoryViewModelFactory(androidContainer.shoppingList)
    }

    @Preview(showSystemUi = true)
    @Composable
    fun MainActivityScreen() {
        val viewModelOwner = LocalViewModelStoreOwner.current!!
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = SHOPPING_LIST_ROUTE,
        ) {
            composable(route = SHOPPING_LIST_ROUTE) {
                val viewModel = viewModel<ShoppingListViewModel>(
                    viewModelOwner,
                    "FirstViewModelInGraph",
                    shoppingListViewModelFactory,
                )
                ShoppingListScreenChangingBetweenModes(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(route = SCREEN_ON_NORMAL_MODE_ROUTE) {
                val viewModel = viewModel<ShoppingListNormalViewModel>(
                    viewModelOwner,
                    "SecondViewModelInGraph",
                    shoppingListNormalViewModelFactory,
                )
                ShoppingListNormalScreen(
                    viewModel = viewModel,
                    navController = navController,
                )
            }
            val selectedItemKey = "selectedItem"
            composable(
                route = "$SHOPPING_LIST_DELETION_ROUTE/{$selectedItemKey}",
                arguments = listOf(
                    navArgument(selectedItemKey) {
                        type = NavType.IntType
                    }
                ),
            ) { backEntry ->
                val viewModel = viewModel<ShoppingListDeletionViewModel>(
                    viewModelOwner,
                    "ThirdViewModelInGraph",
                    shoppingListDeletionViewModelFactory,
                )
                val selectedItemIndex = backEntry.arguments
                    ?.getInt(selectedItemKey)!!
                ShoppingListDeletionScreen(
                    viewModel = viewModel,
                    navController = navController,
                    selectedItemsIndexes = listOf(selectedItemIndex),
                )
            }
            composable(route = PRODUCT_FACTORY_ROUTE) {
                val viewModel = viewModel<ProductFactoryViewModel>(
                    viewModelOwner,
                    "FourthViewModelInGraph",
                    productFactoryViewModelFactory,
                )
                ProductFactoryScreen(navController, viewModel)
            }
        }
    }
}
