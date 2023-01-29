package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import main.shoppilientmobile.android.shoppingList.domain.ShoppingList
import main.shoppilientmobile.android.shoppingList.domain.ShoppingListObserver
import main.shoppilientmobile.domain.Product

class ShoppingListDeletionViewModel(
    private val shoppingList: ShoppingList,
) : ViewModel(), ShoppingListObserver {
    private val _deletableProductItemsUiState = MutableStateFlow(emptyList<DeletableProductItemState>())
    val deletableProductItemsUiState = _deletableProductItemsUiState.asStateFlow()

    init {
        shoppingList.observeShoppingList(this)
    }

    fun deleteProducts() {
        val productsToDelete = _deletableProductItemsUiState.value.asSequence()
            .filter { it.selected }
            .map { it.toProduct() }
            .toList()
        shoppingList.deleteProducts(productsToDelete)
    }

    fun isThisTheLastSelectedItem(productItemIndex: Int): Boolean {
        val numberOfSelectedItems = howManySelectedItemsAreThere()
        return numberOfSelectedItems == 1
    }

    private fun howManySelectedItemsAreThere(): Int {
        return _deletableProductItemsUiState.value.count { it.selected }
    }

    fun selectProductItem(index: Int) {
        sleepUntilShoppingListStateHasBeenLoaded()
        _deletableProductItemsUiState.update {
            it.mapIndexed { indexInList, productItemState ->
                if (indexInList == index) {
                    return@mapIndexed productItemState.copy(selected = true)
                }
                return@mapIndexed productItemState
            }
        }
    }

    fun deselectProductItem(index: Int) {
        sleepUntilShoppingListStateHasBeenLoaded()
        _deletableProductItemsUiState.update {
            it.mapIndexed { indexInList, productItemState ->
                if (indexInList == index) {
                    return@mapIndexed productItemState.copy(selected = false)
                }
                return@mapIndexed productItemState
            }
        }
    }

    fun deselectAllProductItems() {
        _deletableProductItemsUiState.update {
            it.map { it.copy(selected = false) }
        }
    }

    override fun stateAtTheMomentOfSubscribing(currentList: List<Product>) {
        _deletableProductItemsUiState.update {
            val shoppingListCurrentState = currentList.map { product ->
                DeletableProductItemState.fromProduct(product)
            }
            shoppingListCurrentState
        }
    }

    override fun productAdded(product: Product) {
        TODO("Not yet implemented")
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        TODO("Not yet implemented")
    }

    override fun productDeleted(product: Product) {

    }

    private fun sleepUntilShoppingListStateHasBeenLoaded() {
        var shoppingListStateLoaded = false
        while (! shoppingListStateLoaded) {
            if (_deletableProductItemsUiState.value.isNotEmpty()) {
                shoppingListStateLoaded = true
            }
        }
    }
}