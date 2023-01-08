package main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.stateHolders.UserRegistrationViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.IntroduceUserRegistrationCodeRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.composables.routableComposables.RoleElectionRoutableComposable

@Composable
fun RegistrationFeatureNavHost(
    viewModel: UserRegistrationViewModel,
) {
    val navHostController = rememberNavController()
    viewModel.setNavController(navHostController)
    NavHost(
        navController = navHostController,
        startDestination = viewModel.roleElectionComposableRoute,
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
