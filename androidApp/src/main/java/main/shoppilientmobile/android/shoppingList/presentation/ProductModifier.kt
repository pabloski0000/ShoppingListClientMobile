package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.core.composables.TextFieldWithDoneImeAction
import main.shoppilientmobile.shoppingList.infrastructure.presentation.ProductItemState

@Composable
fun ProductModifier(
    modifier: Modifier = Modifier,
    product: ProductItemState,
    onProductChange: (product: ProductItemState) -> Unit,
    onProductModified: (product: ProductItemState) -> Unit,
    onClickOnGoBackIcon: () -> Unit,
) {
    Column(
        modifier = modifier.background(Color(47, 49, 51)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val focusRequester = remember { FocusRequester() }
            val textValue = remember {
                mutableStateOf(TextFieldValue(product.content))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier.clickable { onClickOnGoBackIcon() },
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go Back",
                tint = Color(107, 109, 121),
            )
            Spacer(modifier = Modifier.width(15.dp))
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .shadow(elevation = 0.dp, shape = RoundedCornerShape(10.dp))
                    .padding(end = 10.dp)
                    .testTag("ProductInputText"),
                label = {
                    Text(text = "Modify this product")
                },
                value = textValue.value,
                onValueChange = { changedTextValue ->
                    textValue.value = changedTextValue
                    onProductChange(
                        product.copy(
                            content = changedTextValue.text
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onProductModified(product)
                    }
                ),
            )
            LaunchedEffect(key1 = Unit) {
                awaitFrame()
                delay(100)
                focusRequester.requestFocus()
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
        )
    }
}
