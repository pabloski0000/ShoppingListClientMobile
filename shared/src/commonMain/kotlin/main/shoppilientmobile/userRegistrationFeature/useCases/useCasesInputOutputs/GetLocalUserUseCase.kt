package main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class GetLocalUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun getLocalUser(): User? {
        return try {
            userRepository.getLocalUser()
        } catch (e: Exception) {
            null
        }
    }
}