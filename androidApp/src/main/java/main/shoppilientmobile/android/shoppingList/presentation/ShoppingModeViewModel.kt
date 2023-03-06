package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class ShoppingModeViewModel(
    private val shoppingListUI: AndroidShoppingListUI,
) : ViewModel(), ShoppingListObserver {
    private val _checkableProductStates = MutableStateFlow(
        emptyList<CheckableProductState>()
    )
    val checkableProductStates = _checkableProductStates.asStateFlow()

    suspend fun buyProducts() {
        _checkableProductStates.value.filter { it.checked }
            .map { shoppingListUI.deleteProduct(it.toProduct()) }
    }

    fun changeCheckProduct(index: Int) {
        _checkableProductStates.update {
            it.mapIndexed {productIndex, checkableProductState ->
                if (productIndex == index) {
                    return@mapIndexed checkableProductState.copy(checked = ! checkableProductState.checked)
                }
                return@mapIndexed checkableProductState
            }
        }
    }

    fun observeShoppingList() {
        shoppingListUI.observeShoppingList(this)
    }

    override fun currentState(products: List<Product>) {
        _checkableProductStates.update {
            products.map { CheckableProductState.fromProduct(it) }
        }
    }

    override fun productAdded(product: Product) {
        _checkableProductStates.update { products ->
            listOf(*products.toTypedArray(), CheckableProductState.fromProduct(product))
        }
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        _checkableProductStates.update { products ->
            products.map { product ->
                if (product.toProduct() == oldProduct) {
                    return@map CheckableProductState.fromProduct(newProduct)
                }
                return@map product
            }
        }
    }

    override fun productDeleted(product: Product) {
        _checkableProductStates.update {
            it.filter { checkableProductState -> checkableProductState.toProduct() != product }
        }
    }
}