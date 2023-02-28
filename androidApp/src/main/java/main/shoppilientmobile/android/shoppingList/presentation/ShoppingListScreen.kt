package main.shoppilientmobile.android.shoppingList.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavHostController

const val SHOPPING_LIST_ROUTE = "shopping_list_screen"

@Composable
fun ShoppingListScreen(
    navController: NavHostController,
    viewModel: ShoppingListViewModel,
) {
    val screenState = viewModel.screenStateUiState.collectAsState()

    when (screenState.value) {
        is ShoppingListViewModel.ScreenModeState.NormalModeState -> {
            setUpNormalMode(viewModel)
            ShoppingListScreenOnNormalMode(
                viewModel = viewModel,
                navController = navController,
                onChangeToShoppingListScreenOnModifyingMode = { screenOnNormalModeState ->
                    viewModel.goToModifyingProductScreenMode(screenOnNormalModeState.productToModifyIndex)
                },
                onChangeToShoppingListScreenOnDeletionMode = { screenOnDeletionModeState ->
                    viewModel.goToDeletionScreenMode(screenOnDeletionModeState.selectedProductItemsIndexes.first())
                }
            )
        }
        is ShoppingListViewModel.ScreenModeState.DeletionModeState -> {
            ShoppingListScreenOnDeletionMode(
                viewModel = viewModel,
                deletionModeState = (screenState.value
                        as ShoppingListViewModel.ScreenModeState.DeletionModeState),
                onChangeToShoppingListScreenOnNormalMode = {
                    viewModel.goToNormalScreenMode()
                },
            )
        }
        is ShoppingListViewModel.ScreenModeState.ModifyingProductModeState -> {
            ShoppingListScreenOnModifyingMode(
                viewModel = viewModel,
                modifyingProductModeState = (screenState.value
                        as ShoppingListViewModel.ScreenModeState.ModifyingProductModeState),
                onChangeToShoppingListScreenOnNormalMode = {
                    viewModel.goToNormalScreenMode()
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
        (modifyingProductModeState: ShoppingListViewModel.ScreenModeState.ModifyingProductModeState) -> Unit,
    onChangeToShoppingListScreenOnDeletionMode:
        (deletionModeState: ShoppingListViewModel.ScreenModeState.DeletionModeState) -> Unit,
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
                ShoppingListViewModel.ScreenModeState.ModifyingProductModeState(productItemIndex)
            )
        },
        onLongClickOnProductItem = { productItemIndex ->
            viewModel.selectProductItem(productItemIndex)
            onChangeToShoppingListScreenOnDeletionMode(
                ShoppingListViewModel.ScreenModeState.DeletionModeState(
                    selectedProductItemsIndexes = listOf(productItemIndex)
                )
            )
        },
    )
}

@Composable
private fun ShoppingListScreenOnDeletionMode(
    viewModel: ShoppingListViewModel,
    deletionModeState: ShoppingListViewModel.ScreenModeState.DeletionModeState,
    onChangeToShoppingListScreenOnNormalMode: () -> Unit,
) {
    val productItemsStates = viewModel.productItemsUiState.collectAsState()
    if (! thereIsAtLeastOneProductItemSelected(productItemsStates.value)) {
        onChangeToShoppingListScreenOnNormalMode()
    }

    BackHandler() {
        onChangeToShoppingListScreenOnNormalMode()
    }
    ShoppingListScreenContent(
        topBar = {
            DeletionApplicationTopBar(
                onClickOnDeletionIcon = {
                    viewModel.deleteSelectedProducts()
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
        productModifier = {},
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
    )
}

@Composable
private fun ShoppingListScreenOnModifyingMode(
    viewModel: ShoppingListViewModel,
    modifyingProductModeState: ShoppingListViewModel.ScreenModeState.ModifyingProductModeState,
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
                onClickOnGoBackIcon = onChangeToShoppingListScreenOnNormalMode,
            )
        },
        productItemStates = productItemsState,
        onClickOnAddProductButton = {},
        onClickOnProductItem = {},
        onLongClickOnProductItem = {},
    )
    /*LaunchedEffect(key1 = Unit) {
        if (showKeyboard.value) {
            awaitFrame()
            delay(100)
            focusRequester.requestFocus()
            //showKeyboard.value = false
        }
    }*/
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

private fun setUpShoppingListOnDeletionMode(nominatedProductItemIndexes: List<Int>, viewModel: ShoppingListViewModel) {
    nominatedProductItemIndexes.map { nominatedProduct ->
        viewModel.selectProductItem(nominatedProduct)
    }
}

private data class ShoppingListScreenContentState(
    val productItemStates: List<ProductItemState>,
)