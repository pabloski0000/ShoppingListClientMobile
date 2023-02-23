package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.shoppingList.presentation.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.FillNicknameRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.IntroduceCodeRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.routableComposables.RoleElectionRoutableComposable
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.FillNicknameViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.IntroduceCodeViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.RoleElectionViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.FillNicknameViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHoldersFactories.IntroduceCodeViewModelFactory
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepository
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleRepository
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterUserUseCase

class MainActivity : ComponentActivity() {
    private val activityCreatedCounterBundleKey = "activityCreatedCounter"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app: App
        if (activityIsBeingCreatedForTheFirstTime(savedInstanceState)) {
            app = App(applicationContext)
            (application as AndroidApplication).app = app
            app.run()
        } else {
            app = (application as AndroidApplication).app!!
        }
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    app.getFirstScreen()()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val activityCreatedCounter = outState.getInt(activityCreatedCounterBundleKey)
        outState.putInt(activityCreatedCounterBundleKey, activityCreatedCounter + 1)
    }

    private fun activityIsBeingCreatedForTheFirstTime(savedInstanceState: Bundle?): Boolean {
        return if (savedInstanceState == null) {
            true
        } else {
            savedInstanceState.getInt(activityCreatedCounterBundleKey) > 0
        }
    }
}
