package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    productItemStates: List<ProductItemState>,
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
    productItemState: ProductItemState,
    onLongClick: (ProductItemState) -> Unit,
    onClick: (ProductItemState) -> Unit,
) {
    val modifierDependingIfSelectedOrNot = when (productItemState.markedToBeDeleted) {
        true -> modifier.background(Color.Cyan)
        false -> modifier
    }
    Text(
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
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        text = productItemState.productDescription,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
    )
}