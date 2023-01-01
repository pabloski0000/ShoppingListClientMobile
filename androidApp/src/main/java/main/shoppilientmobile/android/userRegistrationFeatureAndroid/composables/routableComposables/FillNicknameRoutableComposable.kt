package main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModel

interface FillNicknameViewModel {
    fun onNicknameIntroduced(nickname: String)
}

class FillNicknameRoutableComposable: RoutableComposable {
    override val route: String = CompanionObject.route
    companion object CompanionObject {
        const val route = "filling_nickname_screen"
    }

    @Composable
    fun FillNickname(
        viewModel: UserRegistrationViewModel,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(300.dp))

            NicknameField(onDone = { nickname ->
                viewModel.onNicknameIntroduced(nickname)
            })

            Row(
                modifier = Modifier.padding(
                    horizontal = 50.dp
                )
            ) {
                ErrorMessage(errorMessage = viewModel.getErrorMessage().value)
            }
        }
    }

    @Composable
    private fun NicknameField(onDone: (nickname: String) -> Unit) {
        var nickname by remember {
            mutableStateOf("")
        }
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text(text = "Enter your nickname") },
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone(nickname)
                }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
    }
    
    @Composable
    private fun ErrorMessage(
        errorMessage: String,
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
        )
    }
}


