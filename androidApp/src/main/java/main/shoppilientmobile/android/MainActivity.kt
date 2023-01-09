package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.navigation.compose.rememberNavController
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.createListFeature.stateHolders.ShoppingListViewModel
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.composables.RegistrationFeatureNavHost
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.ui.stateHolders.UserRegistrationViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userRegistrationViewModel: UserRegistrationViewModel
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    lateinit var preferencesDataStore: DataStore<Preferences>
    private lateinit var androidContainer: AndroidContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidContainer = (application as AndroidApplication).androidContainer
        val userRegistrationViewModel: UserRegistrationViewModel = viewModels<UserRegistrationViewModel>().value
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val navHostController = rememberNavController()
                    userRegistrationViewModel = UserRegistrationViewModel(
                        registerUserUseCase = androidContainer.registrationContainer,
                        registerAdminUseCase = ,
                        registrationAlerter = ,
                    )
                    RegistrationFeatureNavHost(
                        viewModel = userRegistrationViewModel,
                    )
                }
            }
        }
    }

    private fun buildProject() {
        preferencesDataStore = PreferenceDataStoreFactory.create(
            produceFile = {
                applicationContext.preferencesDataStoreFile("preferences")
            }
        )
        val androidContainer = (application as AndroidApplication).androidContainer
        userRegistrationViewModel = androidContainer.userRegistrationViewModel
        shoppingListViewModel = androidContainer.shoppingListViewModel
    }
}
