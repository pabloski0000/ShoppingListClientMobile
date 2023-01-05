package main.shoppilientmobile.android.createListFeature.stateHolders

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.createListFeature.repositories.ProductRepository

class ShoppingListViewModel(
    private val productRepository: ProductRepository,
): ViewModel() {
    private val productDescriptions = mutableStateOf(emptyList<String>())

    fun getProductDescriptions(): State<List<String>> {
        return productDescriptions
    }
}