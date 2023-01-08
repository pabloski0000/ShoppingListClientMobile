package main.shoppilientmobile.userRegistrationFeature.dataSources

import kotlinx.coroutines.flow.Flow
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.AdminRegistration

interface UserRemoteDataSource {
    suspend fun registerAdmin(user: User): AdminRegistration
    suspend fun registerUser(user: User)
}