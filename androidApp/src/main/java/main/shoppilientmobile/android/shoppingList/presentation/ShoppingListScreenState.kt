package main.shoppilientmobile.android.shoppingList.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShoppingListScreenState {
    private val _shoppingListState: MutableStateFlow<ShoppingListState> =
        MutableStateFlow(ShoppingListState(productItemStates = emptyList()))
    val shoppingListState = _shoppingListState.asStateFlow()
    private val _screenCurrentState: MutableStateFlow<ScreenMode> = MutableStateFlow(ScreenMode.ShoppingList())
    val screenCurrentState = _screenCurrentState.asStateFlow()

    companion object {
        sealed class ScreenMode() {
            class ShoppingList(): ScreenMode()
            class CreatingProduct(): ScreenMode()
            data class ModifyingProduct(val productIndexBeingModified: Int): ScreenMode()
            class DeletionMode(): ScreenMode()
        }
    }

    fun onClickOnAddProduct() {
        _screenCurrentState.update {
            ScreenMode.CreatingProduct()
        }
    }

    fun onClickOnDeletionIcon() {
        val newListOfProducts = shoppingListState.value
            .productItemStates.filter {
                ! it.selected
            }
        _shoppingListState.update {
            it.copy(
                productItemStates = newListOfProducts,
            )
        }
    }

    fun onLongClickOnProduct(indexOnList: Int) {
        val newListOfProducts = shoppingListState.value
            .productItemStates.mapIndexed { index, productItemState ->
                if (index == indexOnList)
                    return@mapIndexed productItemState.copy(selected = true)
                return@mapIndexed productItemState
            }
        _shoppingListState.value = shoppingListState.value.copy(
            productItemStates = newListOfProducts,
        )
        if (shoppingListState.value.productItemStates.isNotEmpty()) {
            _screenCurrentState.update {
                ScreenMode.DeletionMode()
            }
        }
    }

    fun onClickOnProduct(productIndex: Int) {
        _screenCurrentState.update {
            ScreenMode.ModifyingProduct(productIndex)
        }
    }

    fun onProductCreated(product: String) {
        if (_screenCurrentState.value is ScreenMode.CreatingProduct) {
            _shoppingListState.update {
                it.copy(
                    productItemStates = listOf(
                        *shoppingListState.value.productItemStates.toTypedArray(),
                        ProductItemState(product, false),
                    )
                )
            }
        } else if(_screenCurrentState.value is ScreenMode.ModifyingProduct) {
            val modifyingExistingProduct = _screenCurrentState.value as ScreenMode.ModifyingProduct
            val oldProductList = _shoppingListState.value.productItemStates
            val newProductList = oldProductList.mapIndexed { index, productItem ->
                if (index == modifyingExistingProduct.productIndexBeingModified)
                    return@mapIndexed ProductItemState(product, selected = false)
                return@mapIndexed productItem
            }
            _shoppingListState.update {
                it.copy(
                    productItemStates = newProductList,
                )
            }
        }
    }


}