package main.shoppilientmobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    private val activityCreatedCounterBundleKey = "activityCreatedCounter"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = (application as AndroidApplication).runAppIfNotAlreadyInitialised()
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
            savedInstanceState.getInt(activityCreatedCounterBundleKey) < 1
        }
    }
}
