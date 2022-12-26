package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import main.shoppilientmobile.android.views.ChooseAdminOrUserRoleScreen
import main.shoppilientmobile.android.views.IntroduceNicknameScreen
import main.shoppilientmobile.domain.Role

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var roleChosen by remember {
                        mutableStateOf(false)
                    }
                    var role: Role =  remember { Role.BASIC }
                    var nickname: String
                    if (! roleChosen) {
                        ChooseAdminOrUserRoleScreen {
                            role = it
                            roleChosen = true
                        }
                    } else {
                        IntroduceNicknameScreen {
                            nickname = it
                            println("nickname: $nickname, role: ${role.name}")
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
