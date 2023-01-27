package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.UserRole

interface UserRoleLocalDataSource {
    suspend fun add(userRole: UserRole)
    suspend fun getUserRole(): UserRole
}