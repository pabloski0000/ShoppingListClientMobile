package main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.UserRegistrationViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.IntroduceUserRegistrationCodeRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.RoleElectionRoutableComposable

@Composable
fun RegistrationFeatureNavHost(
    viewModel: UserRegistrationViewModel,
) {
    val navHostController = rememberNavController()
    viewModel.setNavController(navHostController)
    NavHost(
        navController = navHostController,
        startDestination = viewModel.getFirstNavigationRoute(),
    ) {
        composable(route = RoleElectionRoutableComposable.route) {
            RoleElectionRoutableComposable.RoleElection(
                viewModel = viewModel,
            )
        }
        composable(route = FillNicknameRoutableComposable.route) {
            FillNicknameRoutableComposable.FillNickname(
                viewModel = viewModel,
            )
        }
        composable(route = IntroduceUserRegistrationCodeRoutableComposable.route) {
            IntroduceUserRegistrationCodeRoutableComposable.IntroduceUserRegistrationCode(
                viewModel = viewModel,
            )
        }
    }
}
