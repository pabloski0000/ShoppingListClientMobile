package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

const val NORMAL_SHOPPING_LIST_ROUTE = "normal_shopping_list_screen"

@Composable
fun ShoppingListNormalScreen(
    viewModel: ShoppingListNormalViewModel,
    navController: NavController,
) {
    val productItemsState = viewModel.productItemsUiState.collectAsState()
    ShoppingListScreenContent(
        topBar = {
            ShoppingListScreenTopBar()
        },
        productModifier = {},
        productItemStates = productItemsState.value,
        onClickOnAddProductButton = {
            navController.navigate(PRODUCT_FACTORY_ROUTE)
        },
        onClickOnProductItem = { productItemIndex ->
        },
        onLongClickOnProductItem = { productItemIndex ->
            navController.navigate("$SHOPPING_LIST_DELETION_ROUTE/$productItemIndex")
        },
    )
}

@Composable
private fun ShoppingListScreenContent(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    productModifier: @Composable () -> Unit,
    productItemStates: List<ProductNormalItemState>,
    onClickOnAddProductButton: () -> Unit,
    onClickOnProductItem: (index: Int) -> Unit,
    onLongClickOnProductItem: (index: Int) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = {
            AddProductButton(
                onClick = {
                    onClickOnAddProductButton()
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
private fun ShoppingList(
    modifier: Modifier = Modifier,
    productItemStates: List<ProductNormalItemState>,
    lazyListState: LazyListState = rememberLazyListState(),
    onLongClickOnProduct: (index: Int) -> Unit = {},
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
fun ProductItem(
    modifier: Modifier = Modifier,
    productItemState: ProductNormalItemState,
    onLongClick: (ProductNormalItemState) -> Unit,
    onClick: (ProductNormalItemState) -> Unit,
) {
    Text(
        modifier = modifier
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
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        text = productItemState.content,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
    )
}