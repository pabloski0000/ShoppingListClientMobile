package main.shoppilientmobile.android.shoppingList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import main.shoppilientmobile.shoppingList.application.UserRegistrationsListener
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class UserRegistrationsViewModel(
    private val androidShoppingListUI: AndroidShoppingListUI,
) : ViewModel(), UserRegistrationsListener {
    private val _userRegistrations = MutableStateFlow(emptyList<Registration>())
    val userRegistrations = _userRegistrations.asStateFlow()
    private var listeningToRegistrations = false

    fun listenToRegistrationsIfNotAlready() {
        if (! listeningToRegistrations) {
            viewModelScope.launch {
                androidShoppingListUI.listenToUserRegistrations(this@UserRegistrationsViewModel)
            }
            listeningToRegistrations = true
        }
    }

    override fun userRegistered(registration: Registration) {
        _userRegistrations.update {
            listOf(*it.toTypedArray(), registration)
        }
    }
}