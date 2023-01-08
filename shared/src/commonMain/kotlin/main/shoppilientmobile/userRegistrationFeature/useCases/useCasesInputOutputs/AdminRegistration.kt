package main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

data class AdminRegistration(val futureUserRegistrations: Flow<Registration>)