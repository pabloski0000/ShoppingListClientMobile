package main.shoppilientmobile.android.createListFeature.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.createListFeature.stateHolders.ShoppingListViewModel

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel,
) {
    val productDescriptions = viewModel.getProductDescriptions()
    ShoppingList(
        productDescriptions = productDescriptions.value,
    )
}

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    productDescriptions: List<String>,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            productDescriptions.map {
                item {
                    ProductItem(
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color.Black,
                        ),
                        description = it,
                        backgroundColor = Color.White,
                    )
                }
            }
        },
    )
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    description: String,
    backgroundColor: Color,
) {
    Text(
        text = description,
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(
                color = backgroundColor,
            ),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun AddProductItemButton(
    modifier: Modifier,
    backgroundColor: Color,
    crossColor: Color,
) {

}

/*@Preview
@Composable
fun Banana() {
    ShoppingList(
        productDescriptions = listOf("Banana", "Tomato"),
    )
}*/
