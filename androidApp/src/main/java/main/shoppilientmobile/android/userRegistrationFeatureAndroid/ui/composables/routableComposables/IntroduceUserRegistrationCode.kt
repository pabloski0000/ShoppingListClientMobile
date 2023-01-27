package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.launch
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel

object IntroduceCodeRoutableComposable: RoutableComposable {
    override val route = "introduce_user_registration_code"

    @Composable
    fun IntroduceCode(
        modifier: Modifier = Modifier,
        viewModel: IntroduceCodeViewModel,
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            val coroutineScope = rememberCoroutineScope()
            val userInformationMessage = viewModel.processInformationUiState.collectAsState().value
            CodeField(
                onCodeIntroduced = { code ->
                    coroutineScope.launch {
                        viewModel.onSignatureIntroduced(signature = code)
                    }
                }
            )
            FillNicknameRoutableComposable.ProcessInformationMessage(
                message = userInformationMessage.message,
                letterColor = userInformationMessage.color,
            )
        }
    }

    @Composable
    fun CodeField(
        modifier: Modifier = Modifier,
        onCodeIntroduced: (code: String) -> Unit,
    ) {
        val code = remember { mutableStateOf("") }
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = code.value,
            onValueChange = { newValue ->
                code.value = newValue
            },
            label = {
                Text(text = "Security Code")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onCodeIntroduced(code.value)
            })
        )
    }
}

