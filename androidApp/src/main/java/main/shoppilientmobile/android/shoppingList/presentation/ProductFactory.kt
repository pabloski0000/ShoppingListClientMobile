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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main.shoppilientmobile.domain.exceptions.ThereCannotBeTwoProductsWithTheSameNameException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionExceedsMaximumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionIsShorterThanMinimumLengthException

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
    val errorMessage = rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    ProductFactoryScreenContent(
        errorMessage = errorMessage.value.ifBlank { null },
        onProductIntroduced = { productToCreate ->
            coroutineScope.launch {
                try {
                    viewModel.createProduct(productToCreate)
                    navController.popBackStack()
                } catch (e: ThereCannotBeTwoProductsWithTheSameNameException) {
                    errorMessage.value = "$productToCreate already exists on shopping list"
                } catch (e: ProductDescriptionExceedsMaximumLengthException) {
                    errorMessage.value = "This product has exceeded the maximum length. Try shortening"
                } catch (e: ProductDescriptionIsShorterThanMinimumLengthException) {
                    errorMessage.value = "This product has a shorter length than the minimum that" +
                            " is required"
                }
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
    errorMessage: String?,
    onProductIntroduced: (product: String) -> Unit,
    keyboardShower: FocusRequester,
) {
    Scaffold(
        topBar = {
            BasicTopBar()
        },
    ) { padding ->
        Column(modifier.padding(padding)) {
            ProductFactory(
                modifier = Modifier.fillMaxWidth(),
                onDone = onProductIntroduced,
                focusRequester = keyboardShower,
            )
            if (errorMessage is String) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                )
            }
        }
    }
}

@Composable
fun ProductFactory(
    modifier: Modifier = Modifier,
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
            onDone = onDone,
            focusRequester = focusRequester,
        )
    }
}

@Composable
private fun ProductFactoryTextField(
    modifier: Modifier = Modifier,
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