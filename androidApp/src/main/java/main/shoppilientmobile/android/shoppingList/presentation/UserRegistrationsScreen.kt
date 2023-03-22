package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.entities.RegistrationCode

const val USER_REGISTRATIONS_SCREEN_ROUTE = "user_registrations_screen"

@Composable
fun UserRegistrationsScreen(
    viewModel: UserRegistrationsViewModel,
) {
    viewModel.listenToRegistrationsIfNotAlready()
    val registrations = viewModel.userRegistrations.collectAsState()
    ScreenContent(registrations = registrations.value)
}

@Composable
private fun ScreenContent(
    registrations: List<Registration>,
) {
    LazyColumn {
        items(registrations.size) {index ->
            val registration = registrations[index]
            RegistrationItem(
                modifier = Modifier.padding(bottom = 15.dp),
                nickname = registration.nickname,
                code = registration.signature!!.value,
            )
        }
    }
}

@Composable
private fun RegistrationItem(
    modifier: Modifier = Modifier,
    nickname: String,
    code: Int,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray),
    ) {
        Text(
            modifier = Modifier.padding(top = 25.dp),
            text = "Nickname: $nickname",
            fontSize = 24.sp,
        )
        Text(
            modifier = Modifier.padding(bottom = 25.dp),
            text = "Code: $code",
            fontSize = 24.sp,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview() {
    ScreenContent(
        registrations = listOf(
            Registration("Manolo", UserRole.BASIC, RegistrationCode(123465)),
            Registration("Manolo", UserRole.BASIC, RegistrationCode(123465)),
            Registration("Manolo", UserRole.BASIC, RegistrationCode(123465)),
            Registration("Manolo", UserRole.BASIC, RegistrationCode(123465)),
        )
    )
}