package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

const val SHOPPING_MODE_SCREEN_ROUTE = "shopping_mode_screen"

@Composable
fun ShoppingModeScreen(
    viewModel: ShoppingModeViewModel,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    val checkableProductStates = viewModel.checkableProductStates.collectAsState()

    ScreenContent(
        checkableProductStates = checkableProductStates.value,
        onClickOnBoughtButton = {
            coroutineScope.launch {
                viewModel.buyProducts()
                navController.popBackStack(SHOPPING_LIST_ROUTE, false)
            }
        },
        onCheckChangeOnProduct = { productIndex, currentlyChecked ->
            viewModel.changeCheckProduct(productIndex)
        }
    )
    LaunchedEffect(key1 = Unit) {
        viewModel.observeShoppingList()

    }
}

@Composable
private fun ScreenContent(
    checkableProductStates: List<CheckableProductState>,
    onClickOnBoughtButton: () -> Unit,
    onCheckChangeOnProduct: (productIndex: Int, currentlyChecked: Boolean) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            BoughtButton(
                onClick = onClickOnBoughtButton,
            )
        },
    ) { padding ->
        CheckableProductList(
            modifier = Modifier.padding(padding),
            checkableProductStates = checkableProductStates,
            onCheckChangeOnProduct = onCheckChangeOnProduct,
        )
    }
}

@Composable
private fun CheckableProductList(
    modifier: Modifier = Modifier,
    checkableProductStates: List<CheckableProductState>,
    onCheckChangeOnProduct: (productIndex: Int, currentlyChecked: Boolean) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(checkableProductStates.size) {index ->
            CheckableProduct(
                checkableProductState = checkableProductStates[index],
                onCheckedStateChange = { currentlyChecked ->
                    onCheckChangeOnProduct(index, currentlyChecked)
                }
            )
        }
    }
}

@Composable
private fun BoughtButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(modifier = Modifier.padding(10.dp), text = "Bought", fontSize = 24.sp)
    }
}

@Composable
private fun CheckableProduct(
    modifier: Modifier = Modifier,
    checkableProductState: CheckableProductState,
    onCheckedStateChange: (currentChecked: Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(8.dp).weight(5f),
            text = checkableProductState.productDescription,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
        )
        Checkbox(
            modifier = Modifier.weight(1f),
            checked = checkableProductState.checked,
            onCheckedChange = onCheckedStateChange,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun anyComposableToPreview() {

}
