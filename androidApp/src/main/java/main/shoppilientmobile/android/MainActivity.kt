package main.shoppilientmobile.android

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import main.shoppilientmobile.Greeting
import main.shoppilientmobile.domain.Role
import main.shoppilientmobile.domain.User
import main.shoppilientmobile.domain.registerUserToList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var nickname by remember {
                        mutableStateOf("")
                    }
                    Column {
                        TextField(
                            value = nickname,
                            onValueChange = {
                                nickname = it
                            },
                            label = {
                                Text(text = "Nickname")
                            }
                        )
                        
                        Button(onClick = {
                            registerUserToList(User(nickname, Role.NORMAL))
                        }) {
                            Text(text = "Register")
                        }
                    }
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
