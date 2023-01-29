package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import main.shoppilientmobile.android.core.composables.TextFieldWithDoneImeAction
import main.shoppilientmobile.domain.Product

@Composable
fun ProductModifier(
    modifier: Modifier = Modifier,
    product: ProductItemState,
    onProductChange: (product: ProductItemState) -> Unit,
    onProductModified: (product: ProductItemState) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
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
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go Back",
                tint = Color(107, 109, 121),
            )
            Spacer(modifier = Modifier.width(15.dp))
            TextFieldWithDoneImeAction(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .shadow(elevation = 0.dp, shape = RoundedCornerShape(10.dp))
                    .padding(end = 10.dp)
                    .testTag("ProductInputText"),
                label = {
                    Text(text = "Modify this product")
                },
                value = product.productDescription,
                onValueChange = { productDescription ->
                    onProductChange(
                        product.copy(
                            productDescription = productDescription
                        )
                    )
                },
                onDone = { productDescription ->
                    onProductModified(
                        product.copy(
                            productDescription = productDescription
                        )
                    )
                },
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
        )
    }
}
