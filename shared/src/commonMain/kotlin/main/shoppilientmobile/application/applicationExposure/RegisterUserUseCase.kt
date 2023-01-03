package main.shoppilientmobile.application.applicationExposure

interface RegisterUserUseCase {
    suspend fun registerUser(user: UserRegistrationData)
}