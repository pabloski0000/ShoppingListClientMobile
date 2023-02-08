package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

const val SHOPPING_LIST_DELETION_ROUTE = "shopping_list_deletion_screen"

@Composable
fun ShoppingListDeletionScreen(
    viewModel: ShoppingListDeletionViewModel,
    navController: NavController,
    selectedItemsIndexes: List<Int> = emptyList(),
) {
    val firstComposition = remember {
        mutableStateOf(true)
    }
    if (firstComposition.value) {
        viewModel.deselectAllProductItems()
        selectedItemsIndexes.map { viewModel.selectProductItem(it) }
        firstComposition.value = false
    }
    val deletableProductItemsState = viewModel.deletableProductItemsUiState.collectAsState()

    ShoppingListDeletionScreenContent(
        deletableProductItemStates = deletableProductItemsState.value,
        onClickOnProductItem = { productItemIndex ->
            if (deletableProductItemsState.value[productItemIndex].selected) {
                val closeScreen = viewModel.isThisTheLastSelectedItem(productItemIndex)
                viewModel.deselectProductItem(productItemIndex)
                if (closeScreen) {
                    navController.popBackStack()
                }
            } else {
                viewModel.selectProductItem(productItemIndex)
            }
        },
        onClickOnDeletionIcon = {
            viewModel.deleteProducts()
            navController.popBackStack()
        },
    )
}

@Composable
private fun ShoppingListDeletionScreenContent(
    deletableProductItemStates: List<DeletableProductItemState>,
    onClickOnProductItem: (productItemIndex: Int) -> Unit,
    onClickOnDeletionIcon: () -> Unit,
) {
    Scaffold(
        topBar = {
             DeletionApplicationTopBar(
                 onClickOnDeletionIcon = onClickOnDeletionIcon,
                 onSelectAllItems = {},
                 onDeselectAllItems = {},
                 allItemsAreSelected = true,
             )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            ShoppingListWithDeletableItems(
                deletableProductItemStates = deletableProductItemStates,
                onClickOnProductItem = onClickOnProductItem,
            )
        }
    }
}

@Composable
private fun ShoppingListWithDeletableItems(
    modifier: Modifier = Modifier,
    deletableProductItemStates: List<DeletableProductItemState>,
    onClickOnProductItem: (productItemIndex: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(deletableProductItemStates.size) { index ->
                val modifierDependingIfSelectedOrNot = when (
                    deletableProductItemStates[index].selected
                ) {
                    true -> modifier.background(Color.Cyan)
                    false -> modifier
                }
                Text(
                    modifier = modifierDependingIfSelectedOrNot.clickable {
                        onClickOnProductItem(index)
                    },
                    text = deletableProductItemStates[index].content,
                )
            }
        },
    )
}