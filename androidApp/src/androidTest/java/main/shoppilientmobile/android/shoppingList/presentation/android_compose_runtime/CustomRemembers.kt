package main.shoppilientmobile.android.shoppingList.presentation.android_compose_runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Test

class CustomRemembers {
    @Test
    fun customRememberSaveable() {
        var numberOfExecutions = 0
        @Composable
        fun SelfExecutableComposable() {
            val anotherStateOfUi: AnotherStateOfUi = rememberAnotherStateOfUi()
            if (numberOfExecutions == 0) {
                SelfExecutableComposable()
                ++numberOfExecutions
            }
        }
    }

    @Test
    fun customRememberSaveableWhenCustomStateHasFlows() {
        var numberOfExecutions = 0
        @Composable
        fun SelfExecutableComposable() {
            val anotherStateOfUi: AnotherStateWithFlowsOfUi = rememberAnotherStateWithFlowsOfUi()
            if (numberOfExecutions == 0) {
                SelfExecutableComposable()
                ++numberOfExecutions
            }
        }
    }

    @Composable
    private fun rememberAnotherStateOfUi(): AnotherStateOfUi {
        return rememberSaveable {
            AnotherStateOfUi(StateOfUi(emptyList()))
        }
    }

    @Composable
    private fun rememberAnotherStateWithFlowsOfUi(): AnotherStateWithFlowsOfUi {
        return rememberSaveable {
            AnotherStateWithFlowsOfUi(MutableStateFlow(AnotherStateOfUi(StateOfUi(emptyList()))))
        }
    }

    data class StateOfUi(val numbers: List<Int>)
    data class AnotherStateOfUi(val stateOfUi: StateOfUi)
    data class AnotherStateWithFlowsOfUi(val states: StateFlow<AnotherStateOfUi>)
}
