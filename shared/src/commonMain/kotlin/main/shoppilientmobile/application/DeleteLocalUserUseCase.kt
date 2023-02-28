package main.shoppilientmobile.application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepository

class DeleteLocalUserUseCase(
    private val userRepository: UserRepository,
) {
    fun deleteLocalUser() {
        runBlocking {
            userRepository.deleteLocalUser()
        }
    }
}