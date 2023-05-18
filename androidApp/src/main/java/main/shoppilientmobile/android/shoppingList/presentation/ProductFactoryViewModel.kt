package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import main.shoppilientmobile.shoppingList.application.RequestExceptionListener

class ProductFactoryViewModel(
    private val shoppingListUI: AndroidShoppingListUI,
    private val shoppingListViewModel: ShoppingListViewModel,
) : ViewModel(), RequestExceptionListener {
    private val _requestState = MutableStateFlow<RequestState?>(null)
    val requestState = _requestState.asStateFlow()
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()
    enum class RequestState {
        INITIALISED,
        ERROR,
        FINISHED_SUCCESSFULLY,
    }


    fun createProductNonBlockingly(product: String) {
        viewModelScope.launch {
            shoppingListViewModel.addProduct(product)
        }
        /*_requestState.update { RequestState.INITIALISED }
        if (_requestState.value != RequestState.ERROR) {
            _errorMessage.update { "" }
            _requestState.update { RequestState.FINISHED_SUCCESSFULLY }
        }*/
        //shoppingListViewModel.addProduct(product)
    }

    override fun informUserOfError(explanation: String) {
        _errorMessage.update { explanation }
        _requestState.update { RequestState.ERROR }
    }
}