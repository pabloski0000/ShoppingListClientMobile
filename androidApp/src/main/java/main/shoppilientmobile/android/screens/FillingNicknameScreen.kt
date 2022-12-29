package main.shoppilientmobile.android.screens

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction

class FillingNicknameScreen: Screen {
    override val route: String = "filling_nickname_screen"

    @Composable
    fun IntroduceNicknameScreen(task: (nickname: String) -> Unit) {
        NicknameTextField(task = task)
    }

    @Composable
    private fun NicknameTextField(task: (nickname: String) -> Unit) {
        val nickname = remember {
            mutableStateOf("")
        }
        TextField(
            value = nickname.value,
            onValueChange = { nickname.value = it },
            label = { Text(text = "Enter your nickname") },
            keyboardActions = KeyboardActions(
                onDone = {
                    task(nickname.value)
                }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
    }
}


