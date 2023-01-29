package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import main.shoppilientmobile.android.shoppingList.domain.ShoppingList

class ShoppingListNormalViewModelFactory(
    private val shoppingList: ShoppingList,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingListNormalViewModel(shoppingList) as T
    }
}