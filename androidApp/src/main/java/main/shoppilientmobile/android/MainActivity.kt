package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.screens.featuresLogic.UserRegistrationLogic

class MainActivity : ComponentActivity() {
    private lateinit var userRegistrationLogic: UserRegistrationLogic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildProject()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    userRegistrationLogic.displayComposable()
                }
            }
        }
    }

    private fun buildProject() {
        val androidContainer = (application as AndroidApplication).androidContainer
        buildUserRegistrationFeature(androidContainer)
    }

    private fun buildUserRegistrationFeature(androidContainer: AndroidContainer) {
        userRegistrationLogic = androidContainer.userRegistrationLogic
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
