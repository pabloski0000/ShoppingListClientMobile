package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.User

interface UserRepository {
    fun registerUser(user: User)
}