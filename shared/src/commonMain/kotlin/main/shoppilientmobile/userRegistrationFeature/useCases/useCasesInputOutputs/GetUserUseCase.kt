package main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs

import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun getUser(): User? {
        return try {
            userRepository.getLocalUser()
        } catch (e: Exception) {
            null
        }
    }
}