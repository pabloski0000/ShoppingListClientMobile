package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import main.shoppilientmobile.application.applicationExposure.Role

interface RoleElectionViewModel {
    fun onRoleChosen(userRole: Role)
}

object RoleElectionRoutableComposable: RoutableComposable {
    override val route: String = "role_election"

    @Composable
    fun RoleElection(
        viewModel: RoleElectionViewModel,
    ) {
        UserChoice(
            onRoleChosen = { role ->
                viewModel.onRoleChosen(role)
            }
        )
    }

    @Composable
    private fun UserChoice(onRoleChosen: (Role) -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AdminButton {
                onRoleChosen(Role.ADMIN)
            }
            UserButton {
                onRoleChosen(Role.BASIC)
            }
        }
    }
}


@Composable
private fun AdminButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Admin")
    }
}

@Composable
private fun UserButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "User")
    }
}

