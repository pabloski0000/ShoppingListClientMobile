package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import main.shoppilientmobile.shoppingList.domain.ShoppingList

class ProductFactoryViewModelFactory(
    private val shoppingList: ShoppingList,
    private val androidShoppingListUI: AndroidShoppingListUI,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //return ProductFactoryViewModel(shoppingList) as T
        return ProductFactoryViewModel(androidShoppingListUI) as T
    }
}