package main.shoppilientmobile.android.shoppingList.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import main.shoppilientmobile.shoppingList.infrastructure.presentation.ProductItemState
import main.shoppilientmobile.shoppingList.infrastructure.presentation.ShoppingListViewModelShared

const val SHOPPING_LIST_ROUTE = "shopping_list_screen"

private sealed class ScreenModeState {
    class NormalModeState() : ScreenModeState()
    data class DeletionModeState(val selectedProductItemsIndexes: List<Int>) : ScreenModeState()
    data class ModifyingProductModeState(val productToModifyIndex: Int) : ScreenModeState()
}

@Composable
fun ShoppingListScreen(
    navController: NavHostController,
    viewModel: ShoppingListViewModelShared,
) {
    val screenState = remember {
        mutableStateOf<ScreenModeState>(ScreenModeState.NormalModeState())
    }
    val productCouldNotBeAddedErrorMessage = viewModel.productCouldNotBeAddedErrorMessage.collectAsState()

    when (screenState.value) {
        is ScreenModeState.NormalModeState -> {
            setUpNormalMode(viewModel)
            ShoppingListScreenOnNormalMode(
                viewModel = viewModel,
                navController = navController,
                productCouldNotBeAddedErrorMessage = productCouldNotBeAddedErrorMessage.value,
                onChangeToShoppingListScreenOnModifyingMode = { screenOnNormalModeState ->
                    screenState.value = ScreenModeState.ModifyingProductModeState(
                        screenOnNormalModeState.productToModifyIndex
                    )
                },
                onChangeToShoppingListScreenOnDeletionMode = { screenOnDeletionModeState ->
                    screenState.value = ScreenModeState.DeletionModeState(
                        screenOnDeletionModeState.selectedProductItemsIndexes
                    )
                },
                onClickOnShoppingCartIcon = {
                    navController.navigate(SHOPPING_MODE_SCREEN_ROUTE)
                }
            )
        }
        is ScreenModeState.DeletionModeState -> {
            ShoppingListScreenOnSelectionMode(
                viewModel = viewModel,
                productCouldNotBeAddedErrorMessage = productCouldNotBeAddedErrorMessage.value,
                onChangeToShoppingListScreenOnNormalMode = {
                    screenState.value = ScreenModeState.NormalModeState()
                },
            )
        }
        is ScreenModeState.ModifyingProductModeState -> {
            ShoppingListScreenOnModifyingMode(
                viewModel = viewModel,
                modifyingProductModeState = (screenState.value
                        as ScreenModeState.ModifyingProductModeState),
                productCouldNotBeAddedErrorMessage = productCouldNotBeAddedErrorMessage.value,
                onChangeToShoppingListScreenOnNormalMode = {
                    screenState.value = ScreenModeState.NormalModeState()
                },
            )
        }
    }
}

private fun setUpNormalMode(viewModel: ShoppingListViewModelShared) {
    viewModel.deselectAllProductItems()
}

@Composable
private fun ShoppingListScreenOnNormalMode(
    viewModel: ShoppingListViewModelShared,
    navController: NavController,
    productCouldNotBeAddedErrorMessage: String,
    onChangeToShoppingListScreenOnModifyingMode:
        (modifyingProductModeState: ScreenModeState.ModifyingProductModeState) -> Unit,
    onChangeToShoppingListScreenOnDeletionMode:
        (deletionModeState: ScreenModeState.DeletionModeState) -> Unit,
    onClickOnShoppingCartIcon: () -> Unit,
) {
    val productItemsState = viewModel.productItemsUiState.collectAsState()
    val userIsAdmin = viewModel.userIsAdmin.collectAsState()
    ShoppingListScreenContent(
        topBar = {
            if (userIsAdmin.value) {
                DefaultTopBar(
                    onClickOnShoppingCartIcon = onClickOnShoppingCartIcon,
                    showNotificationsIcon = true,
                    onClickOnNotifications = {
                        navController.navigate(USER_REGISTRATIONS_SCREEN_ROUTE)
                    },
                )
            } else {
                DefaultTopBar(
                    onClickOnShoppingCartIcon = onClickOnShoppingCartIcon,
                )
            }
        },
        showAddProductButton = true,
        productItemStates = productItemsState.value,
        onClickOnAddProductButton = { screenContentState ->
            navController.navigate(PRODUCT_FACTORY_ROUTE)
        },
        onClickOnProductItem = { productItemIndex ->
            onChangeToShoppingListScreenOnModifyingMode(
                ScreenModeState.ModifyingProductModeState(productItemIndex)
            )
        },
        onLongClickOnProductItem = { productItemIndex ->
            viewModel.selectProductItem(productItemIndex)
            onChangeToShoppingListScreenOnDeletionMode(
                ScreenModeState.DeletionModeState(
                    selectedProductItemsIndexes = listOf(productItemIndex)
                )
            )
        },
        productCouldNotBeAddedErrorMessage = productCouldNotBeAddedErrorMessage,
    )
}

@Composable
private fun ShoppingListScreenOnSelectionMode(
    viewModel: ShoppingListViewModelShared,
    productCouldNotBeAddedErrorMessage: String,
    onChangeToShoppingListScreenOnNormalMode: () -> Unit,
    ) {
    val productItemsStates = viewModel.productItemsUiState.collectAsState()
    if (! thereIsAtLeastOneProductItemSelected(productItemsStates.value)) {
        onChangeToShoppingListScreenOnNormalMode()
    }
    val coroutineScope = rememberCoroutineScope()

    BackHandler() {
        onChangeToShoppingListScreenOnNormalMode()
    }
    ShoppingListScreenContent(
        topBar = {
            ShoppingListActionsTopBar(
                onClickOnDeletionIcon = {
                    coroutineScope.launch {
                        viewModel.deleteSelectedProducts()
                    }
                },
                onSelectAllItems = {
                    viewModel.selectAllProductsItems()
                },
                onDeselectAllItems = {
                    viewModel.deselectAllProductItems()
                },
                allItemsAreSelected = allProductItemsAreSelected(productItemsStates.value),
            )
        },
        showAddProductButton = false,
        productItemStates = productItemsStates.value,
        onClickOnAddProductButton = {},
        onClickOnProductItem = { clickedProductIndex ->
            if (productItemsStates.value[clickedProductIndex].selected) {
                viewModel.deselectProductItem(clickedProductIndex)
            } else {
                viewModel.selectProductItem(clickedProductIndex)
            }
        },
        onLongClickOnProductItem = {},
        productCouldNotBeAddedErrorMessage = productCouldNotBeAddedErrorMessage,
    )
}

@Composable
private fun ShoppingListScreenOnModifyingMode(
    viewModel: ShoppingListViewModelShared,
    modifyingProductModeState: ScreenModeState.ModifyingProductModeState,
    productCouldNotBeAddedErrorMessage: String,
    onChangeToShoppingListScreenOnNormalMode: () -> Unit,
) {
    val productItemsState = remember {
        viewModel.productItemsUiState.value
    }
    val productItemToModify = remember {
        mutableStateOf(
            viewModel.productItemsUiState.value[modifyingProductModeState.productToModifyIndex]
        )
    }
    val errorMessage = remember {
        mutableStateOf("")
    }

    BackHandler() {
        onChangeToShoppingListScreenOnNormalMode()
    }
    ShoppingListScreenContent(
        topBar = {
            DefaultTopBar(
                onClickOnShoppingCartIcon = {},
            )
        },
        showAddProductButton = false,
        productToModify = productItemToModify.value,
        onProductModified = { modifiedProduct ->
            viewModel.modifyProduct(productItemToModify.value.content, modifiedProduct.content) { response ->
                if (response == null) {
                    onChangeToShoppingListScreenOnNormalMode()
                } else {
                    errorMessage.value = response
                }
            }
        },
        errorMessageWhenModifyingProduct = errorMessage.value,
        onClickOnGoBackModifierButton = onChangeToShoppingListScreenOnNormalMode,
        productItemStates = productItemsState,
        onClickOnAddProductButton = {},
        onClickOnProductItem = {},
        onLongClickOnProductItem = {},
        productCouldNotBeAddedErrorMessage = productCouldNotBeAddedErrorMessage,
    )
}

@Composable
private fun ShoppingListScreenContent(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    productToModify: ProductItemState? = null,
    onProductModified: (modifiedProduct: ProductItemState) -> Unit = {},
    onClickOnGoBackModifierButton: () -> Unit = {},
    errorMessageWhenModifyingProduct: String = "",
    showAddProductButton: Boolean,
    productItemStates: List<ProductItemState>,
    onClickOnAddProductButton: (ShoppingListScreenContentState) -> Unit,
    onClickOnProductItem: (index: Int) -> Unit,
    onLongClickOnProductItem: (index: Int) -> Unit,
    productCouldNotBeAddedErrorMessage: String,
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = {
            if (showAddProductButton) {
                AddProductButton(
                    onClick = {
                        onClickOnAddProductButton(ShoppingListScreenContentState(
                            productItemStates,
                        ))
                    }
                )
            }
        },
        scaffoldState = scaffoldState,
    ) { padding ->
        ShoppingList(
            modifier = Modifier.padding(padding),
            productItemStates = productItemStates,
            onLongClickOnProduct = onLongClickOnProductItem,
            onClickOnProduct = onClickOnProductItem
        )
        if (productToModify is ProductItemState) {
            val productToModifyRemembered = remember {
                mutableStateOf(productToModify)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorMessageWhenModifyingProduct,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Color.Red,
                )
                ProductModifier(
                    product = productToModifyRemembered.value,
                    onProductChange = {
                        productToModifyRemembered.value = it
                    },
                    onProductModified = onProductModified,
                    onClickOnGoBackIcon = onClickOnGoBackModifierButton,
                )
            }
        }
        LaunchedEffect(productCouldNotBeAddedErrorMessage) {
            if (productCouldNotBeAddedErrorMessage != "") {
                scaffoldState.snackbarHostState.showSnackbar(productCouldNotBeAddedErrorMessage)
            }
        }
    }
}

@Composable
fun AddProductButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier.testTag("AddProduct"),
        onClick = onClick,
    ) {
        Icon(androidx.compose.material.icons.Icons.Filled.Add, "Localized description")
    }
}

private fun allProductItemsAreSelected(productItemStates: List<ProductItemState>): Boolean {
    return productItemStates.all { it.selected }
}

private fun thereIsAtLeastOneProductItemSelected(
    productItemStates: List<ProductItemState>,
): Boolean {
    return productItemStates.any { it.selected }
}

private data class ShoppingListScreenContentState(
    val productItemStates: List<ProductItemState>,
)