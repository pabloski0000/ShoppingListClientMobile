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
import androidx.compose.runtime.*
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
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val PRODUCT_FACTORY_ROUTE = "product_factory"

@Composable
fun ProductFactoryScreen(
    navController: NavController,
    viewModel: ProductFactoryViewModel,
) {
    val keyboardShower = remember {
        FocusRequester()
    }
    val showKeyboard = remember {
        mutableStateOf(true)
    }
    val product = viewModel.product.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    ProductFactoryScreenContent(
        product = product.value,
        onProductChange = { viewModel.onProductChange(it) },
        onProductIntroduced = { productToCreate ->
            coroutineScope.launch {
                viewModel.createProduct(productToCreate)
                navController.popBackStack()
            }
        },
        keyboardShower = keyboardShower
    )
    LaunchedEffect(key1 = Unit) {
        if (showKeyboard.value) {
            awaitFrame()
            delay(1)
            keyboardShower.requestFocus()
            showKeyboard.value = false
        }
    }
}

@Composable
fun ProductFactoryScreenContent(
    modifier: Modifier = Modifier,
    product: String,
    onProductChange: (product: String) -> Unit,
    onProductIntroduced: (product: String) -> Unit,
    keyboardShower: FocusRequester,
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
                focusRequester = keyboardShower,
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
    focusRequester: FocusRequester,
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
            focusRequester = focusRequester,
        )
    }
}

@Composable
private fun ProductFactoryTextField(
    modifier: Modifier = Modifier,
    product: String,
    onProductChange: (product: String) -> Unit,
    onDone: (product: String) -> Unit,
    focusRequester: FocusRequester,
) {
    val productToCreate = rememberSaveable {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = modifier
            .background(Color(57, 56, 60))
            .focusRequester(focusRequester)
            .testTag("ProductInputText"),
        label = {
            Text(text = "Product Name")
        },
        value = productToCreate.value,
        onValueChange = {
            productToCreate.value = it
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone(productToCreate.value)
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