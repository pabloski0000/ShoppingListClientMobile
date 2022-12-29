package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import main.shoppilientmobile.android.androidApplication.containers.UserRegistrationContainer
import main.shoppilientmobile.android.screensLogic.UserRegistrationLogic

class MainActivity : ComponentActivity() {
    private lateinit var androidApplication: AndroidApplication
    private lateinit var userRegistrationContainer: UserRegistrationContainer
    private lateinit var userRegistrationScreenLogic: UserRegistrationLogic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidApplication = application as AndroidApplication
        userRegistrationContainer = androidApplication.userRegistrationContainer
        userRegistrationScreenLogic = userRegistrationContainer.userRegistrationScreenLogic
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    userRegistrationScreenLogic.displayComposable()
                }
            }
        }
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
