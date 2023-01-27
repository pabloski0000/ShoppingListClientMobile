package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberShoppingListNavController(): NavHostController {
    var navHostController: NavHostController? = null
    if (navHostController == null) {
        navHostController = rememberNavController()
    }
    return navHostController
}
