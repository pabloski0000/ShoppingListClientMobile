package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import main.shoppilientmobile.android.createListFeature.composables.ShoppingList
import main.shoppilientmobile.android.createListFeature.composables.ShoppingListScreen
import main.shoppilientmobile.android.createListFeature.stateHolders.ShoppingListViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.RegistrationFeatureNavHost
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.UserRegistrationViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userRegistrationViewModel: UserRegistrationViewModel
    private lateinit var shoppingListViewModel: ShoppingListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildProject()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    RegistrationFeatureNavHost(
                        viewModel = userRegistrationViewModel,
                    )
                    /*ShoppingListScreen(
                        viewModel = shoppingListViewModel,
                    )*/
                }
            }
        }
    }

    private fun buildProject() {
        val androidContainer = (application as AndroidApplication).androidContainer
        userRegistrationViewModel = androidContainer.userRegistrationViewModel
        shoppingListViewModel = androidContainer.shoppingListViewModel
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
