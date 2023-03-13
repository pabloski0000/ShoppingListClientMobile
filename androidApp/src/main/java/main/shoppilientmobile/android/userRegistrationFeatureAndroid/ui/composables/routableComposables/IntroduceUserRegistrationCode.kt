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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.userRegistrationFeature.useCases.exceptions.WrongCodeException

object IntroduceCodeRoutableComposable: RoutableComposable {
    override val route = "introduce_user_registration_code"

    @Composable
    fun IntroduceCode(
        modifier: Modifier = Modifier,
        viewModel: IntroduceCodeViewModel,
        userNickname: String,
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            val userInformationMessage = viewModel.processInformationUiState.collectAsState().value
            CodeField(
                onCodeIntroduced = { code ->
                    viewModel.confirmRegistration(userNickname, code)
                }
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = userInformationMessage.message,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                color = userInformationMessage.color,
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

