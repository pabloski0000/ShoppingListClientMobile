package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
) {
    Box(
        modifier = modifier.background(Color(47, 49, 51)),
        contentAlignment = Alignment.TopCenter,
    ) {
        Spacer(modifier = Modifier.padding(20.dp))
        TextFieldWithDoneImeAction(
            modifier = Modifier.testTag("ProductInputText"),
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
}
