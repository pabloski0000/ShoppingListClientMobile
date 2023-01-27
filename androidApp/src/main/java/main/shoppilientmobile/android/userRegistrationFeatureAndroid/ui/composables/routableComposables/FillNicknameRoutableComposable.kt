package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables

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
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel

object FillNicknameRoutableComposable: RoutableComposable {
    override val route: String = "fill_nickname"

    @Composable
    fun FillNickname(
        viewModel: FillNicknameViewModel,
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
                val userInformationMessageUiState by viewModel.processInformationUiState.collectAsState()
                ProcessInformationMessage(
                    message = userInformationMessageUiState.message,
                    letterColor = userInformationMessageUiState.color
                )
            }
        }
    }

    @Composable
    private fun NicknameField(onDone: suspend (nickname: String) -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        var nickname by remember {
            mutableStateOf("")
        }
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text(text = "Enter your nickname") },
            keyboardActions = KeyboardActions(
                onDone = {
                    coroutineScope.launch {
                        onDone(nickname)
                    }
                }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
    }

    @Composable
    fun ProcessInformationMessage(
        message: String,
        letterColor: Color
    ) {
        Text(
            text = message,
            color = letterColor,
        )
    }
}


