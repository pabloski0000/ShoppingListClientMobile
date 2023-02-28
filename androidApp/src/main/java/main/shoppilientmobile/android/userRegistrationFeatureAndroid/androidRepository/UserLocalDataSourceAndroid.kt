package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

import main.shoppilientmobile.android.core.room.UserDao
import main.shoppilientmobile.android.core.room.UserEntity
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.UserImpl
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource

class UserLocalDataSourceAndroid(
    private val userDao: UserDao,
) : UserLocalDataSource {
    override suspend fun saveLocalUser(user: User) {
        val userEntity = UserEntity(
            user.getNickname(),
            adaptRoleSoItCanBeSaved(user.getRole()),
            isLocal = true,
        )
        userDao.save(userEntity)
    }

    override suspend fun getLocalUser(): User {
        val userEntity = userDao.getLocalUser()
        return UserBuilderImpl()
            .giveItANickname(userEntity.nickname)
            .setRole(adaptSavedRoleSoItCanBeUsed(userEntity.role))
            .build()
    }

    override suspend fun deleteLocalUser() {
        val userEntity = userDao.getLocalUser()
        userDao.deleteLocalUser()
    }

    private fun adaptRoleSoItCanBeSaved(userRole: UserRole): String {
        return when (userRole) {
            UserRole.BASIC -> "BASIC"
            UserRole.ADMIN -> "ADMIN"
        }
    }

    private fun adaptSavedRoleSoItCanBeUsed(role: String): UserRole {
        return when (role) {
            "BASIC" -> UserRole.BASIC
            "ADMIN" -> UserRole.ADMIN
            else -> throw IllegalArgumentException("Unrecognised role to be adapted")
        }
    }
}