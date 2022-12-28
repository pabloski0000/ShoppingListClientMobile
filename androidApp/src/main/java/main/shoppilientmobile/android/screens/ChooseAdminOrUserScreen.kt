package main.shoppilientmobile.android.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import main.shoppilientmobile.application.applicationExposure.Role

@Composable
fun ChooseAdminOrUserRoleScreen(
    taskAfterRoleElection: (chosenRole: Role) -> Unit,
) {
    UserChoice(taskAfterRoleElection)
}

@Composable
fun UserChoice(taskAfterRoleElection: (Role) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AdminButton {
            taskAfterRoleElection(Role.ADMIN)
        }
        UserButton {
            taskAfterRoleElection(Role.BASIC)
        }
    }
}

@Composable
fun AdminButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Admin")
    }
}

@Composable
fun UserButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "User")
    }
}
