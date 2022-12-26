package main.shoppilientmobile.android.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import main.shoppilientmobile.domain.Role

@Composable
fun ChooseAdminOrUserRoleScreen(taskAfterRoleElection: (Role) -> Unit) {
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
            taskAfterRoleElection(Role.NORMAL)
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
