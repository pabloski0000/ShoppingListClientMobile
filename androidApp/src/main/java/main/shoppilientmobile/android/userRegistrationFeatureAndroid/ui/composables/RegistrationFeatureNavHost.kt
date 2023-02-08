package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel

@Composable
fun RegistrationFeatureNavHost(
    navHostController: NavHostController,
    roleElectionViewModel: RoleElectionViewModel,
    fillNicknameViewModel: FillNicknameViewModel,
) {
    NavHost(
        navController = navHostController,
        startDestination = RoleElectionRoutableComposable.route,
    ) {
        composable(route = RoleElectionRoutableComposable.route) {
            RoleElectionRoutableComposable.RoleElection(
                viewModel = roleElectionViewModel,
                navHostController,
            )
        }
        composable(route = FillNicknameRoutableComposable.route) {
            FillNicknameRoutableComposable.FillNickname(
                viewModel = fillNicknameViewModel,
                navHostController,
            )
        }
    }
}
