package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import main.shoppilientmobile.android.shoppingList.domain.ShoppingList
import main.shoppilientmobile.android.shoppingList.domain.ShoppingListObserver
import main.shoppilientmobile.domain.Product

class ShoppingListNormalViewModel(
    private val shoppingList: ShoppingList,
) : ViewModel(), ShoppingListObserver {
    private val _productItemsUiState = MutableStateFlow(emptyList<ProductNormalItemState>())
    val productItemsUiState = _productItemsUiState.asStateFlow()

    init {
        shoppingList.observeShoppingList(this)
    }

    override fun stateAtTheMomentOfSubscribing(currentList: List<Product>) {
        _productItemsUiState.update {
            currentList.map { product ->
                ProductNormalItemState.fromProduct(product)
            }
        }
    }

    override fun productAdded(product: Product) {
        _productItemsUiState.update {
            listOf(
                ProductNormalItemState.fromProduct(product),
                *it.toTypedArray(),
            )
        }
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        _productItemsUiState.update {
            it.map { product ->
                if (product.toProduct() == oldProduct) {
                    return@map ProductNormalItemState.fromProduct(newProduct)
                }
                return@map product
            }
        }
    }

    override fun productDeleted(product: Product) {
        _productItemsUiState.update {
            it.filter { productItemState -> productItemState.toProduct() != product }
        }
    }
}