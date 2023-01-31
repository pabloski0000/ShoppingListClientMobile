package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*
import main.shoppilientmobile.android.shoppingList.domain.ShoppingList
import main.shoppilientmobile.android.shoppingList.domain.ShoppingListObserver
import main.shoppilientmobile.domain.Product

class ShoppingListViewModel(
    private val shoppingList: ShoppingList,
) : ViewModel(), ShoppingListObserver {
    private val _productItemsUiState = MutableStateFlow(emptyList<ProductItemState>())
    val productItemsUiState = _productItemsUiState.asStateFlow()
    private val _errorMessageUiState = MutableStateFlow(ErrorMessageUiState(""))
    val errorMessageUiState = _errorMessageUiState.asStateFlow()

    init {
        shoppingList.observeShoppingList(this)
    }

    fun selectProductItem(index: Int) {
        _productItemsUiState.update {
            it.mapIndexed { indexInList, productItemState ->
                if (indexInList == index) {
                    return@mapIndexed productItemState.copy(selected = true)
                }
                return@mapIndexed productItemState
            }
        }
    }

    fun deselectProductItem(index: Int) {
        _productItemsUiState.update {
            it.mapIndexed { indexInList, productItemState ->
                if (indexInList == index) {
                    return@mapIndexed productItemState.copy(selected = false)
                }
                return@mapIndexed productItemState
            }
        }
    }

    fun selectAllProductsItems() {
        _productItemsUiState.update {
            it.map { productItem ->
                if (! productItem.selected) {
                    return@map productItem.copy(selected = true)
                }
                return@map productItem
            }
        }
    }

    fun deselectAllProductItems() {
        _productItemsUiState.update {
            it.map { productItem ->
                if (productItem.selected) {
                    return@map productItem.copy(selected = false)
                }
                return@map productItem
            }
        }
    }

    fun modifyProduct(index: Int, newProduct: ProductItemState) {
        shoppingList.modifyProduct(
            oldProduct = _productItemsUiState.value[index].toProduct(),
            newProduct = newProduct.toProduct(),
        )
    }

    fun deleteAllProducts() {
        shoppingList.deleteProducts(_productItemsUiState.value.map { it.toProduct() })
    }

    fun deleteProducts() {
        val productsToDelete = _productItemsUiState.value.filter { it.selected }
        shoppingList.deleteProducts(
            productsToDelete.map { it.toProduct() }
        )
    }

    override fun stateAtTheMomentOfSubscribing(currentList: List<Product>) {
        _productItemsUiState.update {
            currentList.map { product ->
                ProductItemState(product.description, false)
            }
        }
    }

    override fun productAdded(product: Product) {
        _productItemsUiState.update {
            listOf(
                ProductItemState(product.description, false),
                *it.toTypedArray(),
            )
        }
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        _productItemsUiState.update {
            it.map { product ->
                if (product.toProduct() == oldProduct) {
                    return@map ProductItemState(newProduct.description, false)
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