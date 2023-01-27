package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import main.shoppilientmobile.domain.Product


const val SHOPPING_LIST_ROUTE = "shopping_list"

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
    ShoppingListScreenContent(
        topBar = {
             if (screenMode.value == ScreenMode.DELETION) {
                 DeletionApplicationTopBar(
                     onClickOnDeletionIcon = {
                         if (screenMode.value == ScreenMode.DELETION) {
                             viewModel.deleteProducts()
                         }
                     }
                 )
             } else {
                 DefaultApplicationTopBar()
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
                        viewModel.modifyProduct(
                            index = productToModifyIndex.value,
                            newProduct = productItemToModify.value,
                        )
                    }
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
                    if (isThereAnyNominatedProductItemLeft(productItemsUiState.value)) {
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
fun ShoppingList(
    modifier: Modifier = Modifier,
    productItemStates: List<ProductItemState>,
    lazyListState: LazyListState = rememberLazyListState(),
    onLongClickOnProduct: (index: Int) -> Unit,
    onClickOnProduct: (index: Int) -> Unit,
) {

    LazyColumn(
        modifier = modifier,
        content = {
            items(productItemStates.size) { index ->
                ProductItem(
                    productItemState = productItemStates[index],
                    onLongClick = { itemState ->
                        onLongClickOnProduct(index)
                    },
                    onClick = { itemState ->
                        onClickOnProduct(index)
                    },
                )
            }
        },
        state = lazyListState,
    )
}

@Composable
fun CodeField(
    modifier: Modifier = Modifier,
    onCodeIntroduced: (code: String) -> Unit,
) {
    val code = remember { mutableStateOf("") }
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = code.value,
        onValueChange = { newValue ->
            code.value = newValue
        },
        label = {
            Text(text = "Security Code")
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onCodeIntroduced(code.value)
        })
    )
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    productItemState: ProductItemState,
    onLongClick: (ProductItemState) -> Unit,
    onClick: (ProductItemState) -> Unit,
) {
    val modifierDependingIfSelectedOrNot = when (productItemState.markedToBeDeleted) {
        true -> modifier.background(Color.Cyan)
        false -> modifier
    }
    Text(
        text = productItemState.productDescription,
        modifier = modifierDependingIfSelectedOrNot
            .testTag("Product")
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick(productItemState)
                    },
                    onTap = {
                        onClick(productItemState)
                    },
                )
            }
            .padding(8.dp),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
    )
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

private fun isThereAnyNominatedProductItemLeft(
    productItemStates: List<ProductItemState>,
): Boolean {
    var thereIsNominatedProductItems = false
    productItemStates.map {
        if (it.markedToBeDeleted) {
            thereIsNominatedProductItems = true
        }
    }
    return thereIsNominatedProductItems
}

private data class ShoppingListScreenContentState(
    val productItemStates: List<ProductItemState>,
)
private enum class ScreenMode {
    NORMAL,
    DELETION,
    MODIFYING_PRODUCT,
}

@Preview(showSystemUi = true)
@Composable
fun ShoppingList() {

}
