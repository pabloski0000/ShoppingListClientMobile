package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductFactoryViewModel(
    private val shoppingListUI: AndroidShoppingListUI,
) : ViewModel() {

    suspend fun createProduct(product: String) {
        shoppingListUI.addProduct(Product(product))
    }
}