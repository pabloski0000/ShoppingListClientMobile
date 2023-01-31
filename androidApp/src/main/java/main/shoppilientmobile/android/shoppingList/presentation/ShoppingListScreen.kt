package main.shoppilientmobile.android.shoppingList.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay


const val SHOPPING_LIST_ROUTE = "shopping_list"
const val SCREEN_ON_NORMAL_MODE_ROUTE = "normal_screen_mode"
private const val SCREEN_ON_DELETION_MODE_ROUTE = "deletion_screen_mode"
private const val SCREEN_ON_MODIFYING_PRODUCT_MODE_ROUTE = "modifying_product_mode"

@Composable
fun ShoppingListScreenChangingBetweenModes(
    navController: NavHostController,
    viewModel: ShoppingListViewModel,
) {
    val screenState = remember {
        mutableStateOf(
            ScreenState(ScreenMode.NORMAL, ScreenModeState.NormalModeState())
        )
    }

    when (screenState.value.screenMode) {
        ScreenMode.NORMAL -> {
            setUpNormalMode(viewModel)
            ShoppingListScreenOnNormalMode(
                viewModel = viewModel,
                navController = navController,
                onChangeToShoppingListScreenOnModifyingMode = { screenOnNormalModeState ->
                    screenState.value = ScreenState(
                        ScreenMode.MODIFYING_PRODUCT,
                        screenOnNormalModeState,
                    )
                },
                onChangeToShoppingListScreenOnDeletionMode = { screenOnDeletionModeState ->
                    screenState.value = ScreenState(
                        ScreenMode.DELETION,
                        screenOnDeletionModeState,
                    )
                }
            )
        }
        ScreenMode.DELETION -> {
            ShoppingListScreenOnDeletionMode(
                viewModel = viewModel,
                deletionModeState = screenState.value.screenModeState
                        as ScreenModeState.DeletionModeState,
                onChangeToShoppingListScreenOnNormalMode = {
                    screenState.value = ScreenState(
                        ScreenMode.NORMAL,
                        ScreenModeState.NormalModeState(),
                    )
                },
            )
        }
        ScreenMode.MODIFYING_PRODUCT -> {
            ShoppingListScreenOnModifyingMode(
                viewModel = viewModel,
                modifyingProductModeState = screenState.value.screenModeState
                        as ScreenModeState.ModifyingProductModeState,
                onChangeToShoppingListScreenOnNormalMode = {
                    screenState.value = ScreenState(
                        ScreenMode.NORMAL,
                        ScreenModeState.NormalModeState(),
                    )
                },
            )
        }
    }
}

private fun setUpNormalMode(viewModel: ShoppingListViewModel) {
    viewModel.deselectAllProductItems()
}

@Composable
private fun ShoppingListScreenOnNormalMode(
    viewModel: ShoppingListViewModel,
    navController: NavController,
    onChangeToShoppingListScreenOnModifyingMode:
        (modifyingProductModeState: ScreenModeState.ModifyingProductModeState) -> Unit,
    onChangeToShoppingListScreenOnDeletionMode: (deletionModeState: ScreenModeState.DeletionModeState) -> Unit,
) {
    val productItemsState = viewModel.productItemsUiState.collectAsState()
    ShoppingListScreenContent(
        topBar = {
            ShoppingListScreenTopBar()
        },
        productModifier = {},
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
                    nominatedProductItemIndexes = listOf(productItemIndex)
                )
            )
        },
    )
}

@Composable
private fun ShoppingListScreenOnDeletionMode(
    viewModel: ShoppingListViewModel,
    deletionModeState: ScreenModeState.DeletionModeState,
    onChangeToShoppingListScreenOnNormalMode: () -> Unit,
) {
    val productItemsState = viewModel.productItemsUiState.collectAsState()
    if (! areThereNominatedProductItems(productItemsState.value)) {
        onChangeToShoppingListScreenOnNormalMode()
    }

    BackHandler() {
        onChangeToShoppingListScreenOnNormalMode()
    }
    ShoppingListScreenContent(
        topBar = {
            DeletionApplicationTopBar(
                onClickOnDeletionIcon = {
                    viewModel.deleteProducts()
                },
                onSelectAllItems = {
                    viewModel.selectAllProductsItems()
                },
            )
        },
        productModifier = {},
        productItemStates = productItemsState.value,
        onClickOnAddProductButton = {},
        onClickOnProductItem = { clickedProductIndex ->
            if (productItemsState.value[clickedProductIndex].selected) {
                viewModel.deselectProductItem(clickedProductIndex)
            } else {
                viewModel.selectProductItem(clickedProductIndex)
            }
        },
        onLongClickOnProductItem = {},
    )
}

@Composable
private fun ShoppingListScreenOnModifyingMode(
    viewModel: ShoppingListViewModel,
    modifyingProductModeState: ScreenModeState.ModifyingProductModeState,
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
    val focusRequester = remember {
        FocusRequester()
    }
    val showKeyboard = remember {
        mutableStateOf(true)
    }

    BackHandler() {
        onChangeToShoppingListScreenOnNormalMode()
    }
    ShoppingListScreenContent(
        topBar = {
            ShoppingListScreenTopBar()
        },
        productModifier = {
            ProductModifier(
                product = productItemToModify.value,
                onProductChange = {
                    productItemToModify.value = it
                },
                onProductModified = {
                    viewModel.modifyProduct(
                        index = modifyingProductModeState.productToModifyIndex,
                        newProduct = productItemToModify.value,
                    )
                    onChangeToShoppingListScreenOnNormalMode()
                },
                focusRequester = focusRequester,
                onClickOnGoBackIcon = onChangeToShoppingListScreenOnNormalMode,
            )
        },
        productItemStates = productItemsState,
        onClickOnAddProductButton = {},
        onClickOnProductItem = {},
        onLongClickOnProductItem = {},
    )
    LaunchedEffect(key1 = Unit) {
        if (showKeyboard.value) {
            awaitFrame()
            delay(100)
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun ShoppingListScreenContent(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    productModifier: @Composable () -> Unit,
    productItemStates: List<ProductItemState>,
    onClickOnAddProductButton: (ShoppingListScreenContentState) -> Unit,
    onClickOnProductItem: (index: Int) -> Unit,
    onLongClickOnProductItem: (index: Int) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = {
            AddProductButton(
                onClick = {
                    onClickOnAddProductButton(ShoppingListScreenContentState(
                        productItemStates,
                    ))
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            ShoppingList(
                productItemStates = productItemStates,
                onLongClickOnProduct = onLongClickOnProductItem,
                onClickOnProduct = onClickOnProductItem
            )
            productModifier()
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
        Icon(Icons.Filled.Add, "Localized description")
    }
}

private fun areThereNominatedProductItems(
    productItemStates: List<ProductItemState>,
): Boolean {
    return productItemStates.any { it.selected }
}

private fun setUpShoppingListOnDeletionMode(nominatedProductItemIndexes: List<Int>, viewModel: ShoppingListViewModel) {
    nominatedProductItemIndexes.map { nominatedProduct ->
        viewModel.selectProductItem(nominatedProduct)
    }
}

private data class ShoppingListScreenContentState(
    val productItemStates: List<ProductItemState>,
)

private data class ScreenState(
    val screenMode: ScreenMode,
    val screenModeState: ScreenModeState
    )
private sealed class ScreenModeState {
    class NormalModeState() : ScreenModeState()
    data class DeletionModeState(val nominatedProductItemIndexes: List<Int>) : ScreenModeState()
    data class ModifyingProductModeState(val productToModifyIndex: Int) : ScreenModeState()
}
private enum class ScreenMode {
    NORMAL,
    DELETION,
    MODIFYING_PRODUCT,
}

