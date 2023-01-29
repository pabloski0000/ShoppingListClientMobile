package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay


const val SHOPPING_LIST_ROUTE = "shopping_list"
private const val screenOnNormalModeRoute = "normal_screen_mode"
private const val screenOnDeletionModeRoute = "deletion_screen_mode"
private const val screenOnModifyingProductModeRoute = "modifying_product_mode"

@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel,
) {
    val productItemsUiState = viewModel.productItemsUiState.collectAsState()
    val screenMode = rememberSaveable {
        mutableStateOf(ScreenMode.NORMAL)
    }
    val productToModifyIndex = remember {
        mutableStateOf(-1)
    }
    val focusRequester = remember {
        FocusRequester()
    }
    ShoppingListScreenContent(
        topBar = {
             if (screenMode.value == ScreenMode.DELETION) {
                 DeletionApplicationTopBar(
                     onClickOnDeletionIcon = {
                         if (screenMode.value == ScreenMode.DELETION) {
                             viewModel.deleteProducts()
                             screenMode.value = ScreenMode.NORMAL
                         }
                     }
                 )
             } else {
                 ShoppingListScreenTopBar(
                     onRemoveIcon = {
                         viewModel.deleteAllProducts()
                     }
                 )
             }
        },
        productModifier = {
            if (screenMode.value == ScreenMode.MODIFYING_PRODUCT) {
                val productItemToModify = remember {
                    mutableStateOf(productItemsUiState.value[productToModifyIndex.value])
                }
                ProductModifier(
                    product = productItemToModify.value,
                    onProductChange = {
                        productItemToModify.value = it
                    },
                    onProductModified = {
                        screenMode.value = ScreenMode.NORMAL
                        viewModel.modifyProduct(
                            index = productToModifyIndex.value,
                            newProduct = productItemToModify.value,
                        )
                    },
                )
            }
        },
        productItemStates = productItemsUiState.value,
        onClickOnAddProductButton = { screenContentState ->
            if (screenMode.value == ScreenMode.NORMAL) {
                navController.navigate(PRODUCT_FACTORY_ROUTE)
            }
        },
        onClickOnProductItem = { clickedProductIndex ->
            if (screenMode.value == ScreenMode.DELETION) {
                if (productItemsUiState.value[clickedProductIndex].markedToBeDeleted) {
                    viewModel.unnominateProductItem(clickedProductIndex)
                    if (! areThereNominatedProductItems(productItemsUiState.value)) {
                        screenMode.value = ScreenMode.NORMAL
                    }
                } else {
                    viewModel.nominateProductItem(clickedProductIndex)
                }
            } else if (screenMode.value == ScreenMode.NORMAL) {
                screenMode.value = ScreenMode.MODIFYING_PRODUCT
                productToModifyIndex.value = clickedProductIndex
            }
        },
        onLongClickOnProductItem = { index ->
            if (screenMode.value == ScreenMode.NORMAL) {
                screenMode.value = ScreenMode.DELETION
                viewModel.nominateProductItem(index)
            }
        },
    )
}

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
                }
            )
        }
    }
}

@Composable
fun ShoppingListScreenToNavigateToShoppingListDeletionScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel,
) {
    ShoppingListScreenOnNormalMode(
        viewModel = viewModel,
        navController = navController,
        onChangeToShoppingListScreenOnModifyingMode = {},
        onChangeToShoppingListScreenOnDeletionMode = { screenOnDeletionModeState ->
            navController.navigate("$SHOPPING_LIST_DELETION_ROUTE/4")
        }
    )
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
            ShoppingListScreenTopBar(
                onRemoveIcon = {
                    viewModel.deleteAllProducts()
                }
            )
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
            viewModel.nominateProductItem(productItemIndex)
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

    ShoppingListScreenContent(
        topBar = {
            DeletionApplicationTopBar(
                onClickOnDeletionIcon = {
                    viewModel.deleteProducts()
                }
            )
        },
        productModifier = {},
        productItemStates = productItemsState.value,
        onClickOnAddProductButton = {},
        onClickOnProductItem = { clickedProductIndex ->
            if (productItemsState.value[clickedProductIndex].markedToBeDeleted) {
                viewModel.unnominateProductItem(clickedProductIndex)
            } else {
                viewModel.nominateProductItem(clickedProductIndex)
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

    ShoppingListScreenContent(
        topBar = {
            ShoppingListScreenTopBar(
                onRemoveIcon = {}
            )
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
            delay(1)
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
    return productItemStates.any { it.markedToBeDeleted }
}

private fun setUpShoppingListOnDeletionMode(nominatedProductItemIndexes: List<Int>, viewModel: ShoppingListViewModel) {
    nominatedProductItemIndexes.map { nominatedProduct ->
        viewModel.nominateProductItem(nominatedProduct)
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

