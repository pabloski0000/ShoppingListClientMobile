package main.shoppilientmobile.android.shoppingList.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Add
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
import main.shoppilientmobile.domain.exceptions.ProductDescriptionExceedsMaximumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionIsShorterThanMinimumLengthException
import main.shoppilientmobile.domain.exceptions.ThereCannotBeTwoProductsWithTheSameNameException

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
                },
                onClickOnShoppingCartIcon = {
                    navController.navigate(SHOPPING_MODE_SCREEN_ROUTE)
                }
            )
        }
        is ShoppingListViewModel.ScreenModeState.DeletionModeState -> {
            ShoppingListScreenOnSelectionMode(
                viewModel = viewModel,
                state = (screenState.value
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
    onClickOnShoppingCartIcon: () -> Unit,
) {
    val productItemsState = viewModel.productItemsUiState.collectAsState()
    ShoppingListScreenContent(
        topBar = {
            ShoppingListScreenTopBar(
                onClickOnShoppingCartIcon = onClickOnShoppingCartIcon,
            )
        },
        showAddProductButton = true,
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
private fun ShoppingListScreenOnSelectionMode(
    viewModel: ShoppingListViewModel,
    state: ShoppingListViewModel.ScreenModeState.DeletionModeState,
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
    val errorMessage = remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()

    BackHandler() {
        onChangeToShoppingListScreenOnNormalMode()
    }
    ShoppingListScreenContent(
        topBar = {
            ShoppingListScreenTopBar(
                onClickOnShoppingCartIcon = {},
            )
        },
        showAddProductButton = false,
        productToModify = productItemToModify.value,
        onProductModified = { modifiedProduct ->
            coroutineScope.launch {
                try {
                    viewModel.modifyProduct(
                        index = modifyingProductModeState.productToModifyIndex,
                        newProduct = modifiedProduct,
                    )
                    onChangeToShoppingListScreenOnNormalMode()
                } catch (e: ThereCannotBeTwoProductsWithTheSameNameException) {
                    errorMessage.value = "There is another product with that description"
                } catch (e: ProductDescriptionExceedsMaximumLengthException) {
                    errorMessage.value = "Try shortening it. It is too long"
                } catch (e: ProductDescriptionIsShorterThanMinimumLengthException) {
                    errorMessage.value = "It has too few character. Make it longer!!!!"
                }
            }
        },
        errorMessageWhenModifyingProduct = errorMessage.value,
        onClickOnGoBackModifierButton = onChangeToShoppingListScreenOnNormalMode,
        productItemStates = productItemsState,
        onClickOnAddProductButton = {},
        onClickOnProductItem = {},
        onLongClickOnProductItem = {},
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
) {
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
                //productModifier(modifier = Modifier.align(Alignment.BottomCenter))
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