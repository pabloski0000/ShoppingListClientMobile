package main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.RoleElectionRoutableComposable

@Composable
fun RegistrationFeatureNavHost(
    viewModel: UserRegistrationViewModel,
) {
    val navHostController = rememberNavController()
    viewModel.setNavController(navHostController)
    val roleElectionRoutableComposable = RoleElectionRoutableComposable()
    val fillNicknameRoutableComposable = FillNicknameRoutableComposable()
    NavHost(
        navController = navHostController,
        startDestination = RoleElectionRoutableComposable.route,
    ) {
        composable(route = roleElectionRoutableComposable.route) {
            roleElectionRoutableComposable.RoleElection(
                viewModel = viewModel,
            )
        }
        composable(route = fillNicknameRoutableComposable.route) {
            fillNicknameRoutableComposable.FillNickname(
                viewModel = viewModel
            )
        }
    }
}
