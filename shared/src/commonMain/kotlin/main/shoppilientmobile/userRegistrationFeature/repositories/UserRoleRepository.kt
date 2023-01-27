package main.shoppilientmobile.userRegistrationFeature.repositories

import main.shoppilientmobile.domain.domainExposure.UserRole

class UserRoleRepository(
    private val userRoleLocalDataSource: UserRoleLocalDataSource,
) {
    suspend fun add(userRole: UserRole) = userRoleLocalDataSource.add(userRole)
    suspend fun getUserRoleRepository(): UserRole = userRoleLocalDataSource.getUserRole()
}