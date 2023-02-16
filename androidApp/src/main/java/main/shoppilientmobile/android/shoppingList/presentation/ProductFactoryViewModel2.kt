package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductFactoryViewModel2(
    private val shoppingListUI: AndroidShoppingListUI,
) : ViewModel() {
    private val _product = MutableStateFlow("")
    val product = _product.asStateFlow()

    fun onProductChange(product: String) {
        _product.update { product }
    }

    fun createProduct(product: String) {
        shoppingListUI.addProduct(Product(product))
    }
}