package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

const val PRODUCT_FACTORY_ROUTE = "product_factory"

@Composable
fun ProductFactoryScreen(
    navController: NavController,
    viewModel: ProductFactoryViewModel,
) {
    val product = viewModel.product.collectAsState()
    ProductFactoryScreenContent(
        product = product.value,
        onProductChange = { viewModel.onProductChange(it) },
        onProductIntroduced = {
            viewModel.createProduct()
            navController.popBackStack()
        },
    )
}

@Composable
fun ProductFactoryScreenContent(
    modifier: Modifier = Modifier,
    product: String,
    onProductChange: (product: String) -> Unit,
    onProductIntroduced: (product: String) -> Unit,
) {
    Scaffold(
        topBar = {
            DefaultApplicationTopBar()
        },
    ) { padding ->
        Column(modifier.padding(padding)) {
            ProductFactory(
                modifier = Modifier.fillMaxWidth(),
                product = product,
                onProductChange = onProductChange,
                onDone = onProductIntroduced,
            )
        }
    }
}

@Composable
fun ProductFactory(
    modifier: Modifier = Modifier,
    product: String,
    onProductChange: (product: String) -> Unit,
    onDone: (product: String) -> Unit,
) {
    ProductFactoryBackground(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        ProductFactoryTextField(
            modifier = Modifier
                .padding(20.dp),
            product = product,
            onProductChange = onProductChange,
            onDone = onDone,
        )
    }
}

@Composable
private fun ProductFactoryTextField(
    modifier: Modifier = Modifier,
    product: String,
    onProductChange: (product: String) -> Unit,
    onDone: (product: String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .background(Color(57, 56, 60))
            .testTag("ProductInputText"),
        label = {
            Text(text = "Product Name")
        },
        value = product,
        onValueChange = onProductChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone(product)
            },
        ),
    )
}

@Composable
private fun ProductFactoryBackground(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.background(Color(47, 49, 51)),
        contentAlignment = contentAlignment,
    ) {
        content()
    }
}