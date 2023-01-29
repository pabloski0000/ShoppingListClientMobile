package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import main.shoppilientmobile.android.shoppingList.data.AndroidShoppingList
import main.shoppilientmobile.android.shoppingList.domain.ProductDeleter
import main.shoppilientmobile.android.shoppingList.domain.ShoppingList

class ShoppingListDeletionViewModelFactory(
    private val shoppingList: ShoppingList,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingListDeletionViewModel(shoppingList) as T
    }
}