package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShoppingListViewModel2(
    private val shoppingListUI: AndroidShoppingListUI,
) : ViewModel(), ShoppingListObserver {
    private val _productItemsUiState = MutableStateFlow(emptyList<ProductItemState>())
    val productItemsUiState = _productItemsUiState.asStateFlow()
    private val _screenStateUiState: MutableStateFlow<ScreenModeState> = MutableStateFlow(
        ScreenModeState.NormalModeState()
    )
    val screenStateUiState = _screenStateUiState.asStateFlow()
    private val _errorMessageUiState = MutableStateFlow(ErrorMessageUiState(""))
    val errorMessageUiState = _errorMessageUiState.asStateFlow()
    private data class ScreenState(
        val screenMode: ScreenMode2,
        val screenModeState: ScreenModeState,
    )
    sealed class ScreenModeState {
        class NormalModeState() : ScreenModeState()
        data class DeletionModeState(val selectedProductItemsIndexes: List<Int>) : ScreenModeState()
        data class ModifyingProductModeState(val productToModifyIndex: Int) : ScreenModeState()
    }
    enum class ScreenMode2 {
        NORMAL,
        DELETION,
        MODIFYING_PRODUCT,
    }

    init {
        shoppingListUI.observeShoppingList(this)
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
        val productToModify = _productItemsUiState.value[index].toProduct2()
        val modifiedProduct = newProduct.toProduct2()
        shoppingListUI.modifyProduct(productToModify, modifiedProduct)
    }

    fun deleteAllProducts() {
        _productItemsUiState.value.map { productItem: ProductItemState ->
            shoppingListUI.deleteProduct(productItem.toProduct2())
        }
    }

    fun deleteSelectedProducts() {
        val selectedProducts = _productItemsUiState.value.filter { it.selected }
        selectedProducts.map { productItem: ProductItemState ->
            shoppingListUI.deleteProduct(productItem.toProduct2())
        }
    }

    fun goToNormalScreenMode() {
        _screenStateUiState.update { ScreenModeState.NormalModeState() }
    }

    fun goToDeletionScreenMode(productToSelectIndex: Int) {
        _screenStateUiState.update {
            ScreenModeState.DeletionModeState(
                selectedProductItemsIndexes = listOf(productToSelectIndex)
            )
        }
    }

    fun goToModifyingProductScreenMode(productToModifyIndex: Int) {
        _screenStateUiState.update { ScreenModeState.ModifyingProductModeState(productToModifyIndex) }
    }

    override fun currentState(products: List<Product>) {
        _productItemsUiState.update {
            products.map { product ->
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
                if (product.toProduct2() == oldProduct) {
                    return@map ProductItemState(newProduct.description, false)
                }
                return@map product
            }
        }
    }

    override fun productDeleted(product: Product) {
        _productItemsUiState.update {
            it.filter { productItemState -> productItemState.toProduct2() != product }
        }
    }
}