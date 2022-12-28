package main.shoppilientmobile.android.screensLogic

import androidx.compose.runtime.*
import main.shoppilientmobile.application.applicationExposure.RegisterUserUseCase
import main.shoppilientmobile.application.applicationExposure.UserRegistrationData
import main.shoppilientmobile.application.applicationExposure.Role

class RegisterUserScreenLogic(
    private val RoleElectionScreen: @Composable ( (Role) -> Unit ) -> Unit,
    private val NicknameInsertionScreen: @Composable ( (nickname: String) -> Unit ) -> Unit,
    private val registerUserUseCase: RegisterUserUseCase,
) {
    @Composable
    fun display() {
        var roleChosen by remember {
            mutableStateOf(false)
        }
        var role: Role =  remember { Role.BASIC }
        if (! roleChosen) {
            RoleElectionScreen {
                role = it
                roleChosen = true
            }
        } else {
            NicknameInsertionScreen { nickname ->
                registerUserUseCase.registerUser(
                    UserRegistrationData(nickname, role)
                )
            }
        }
    }
}