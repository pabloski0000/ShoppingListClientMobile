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
import androidx.navigation.compose.rememberNavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.RegistrationFeatureNavHost
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModelBuilder

class MainActivity : ComponentActivity() {
    private lateinit var userRegistrationViewModel: UserRegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildProject()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    RegistrationFeatureNavHost(viewModel = userRegistrationViewModel)
                }
            }
        }
    }

    private fun buildProject() {
        val androidContainer = (application as AndroidApplication).androidContainer
        userRegistrationViewModel = androidContainer.userRegistrationViewModel
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
