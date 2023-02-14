package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import main.shoppilientmobile.shoppingList.domain.ShoppingList
import main.shoppilientmobile.domain.Product

class ProductFactoryViewModel(
    private val shoppingList: ShoppingList,
) : ViewModel() {
    private val _product = MutableStateFlow("")
    val product = _product.asStateFlow()

    fun onProductChange(product: String) {
        _product.update { product }
    }

    fun createProduct(product: String) {
        shoppingList.addProduct(Product(product))
    }
}