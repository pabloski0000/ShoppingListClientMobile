package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User

interface UserRepository {
    suspend fun registerUser(user: User)
}