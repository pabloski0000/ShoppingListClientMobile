package main.shoppilientmobile.android.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRoleLocalDataSource

@Dao
interface UserRoleDao : UserRoleLocalDataSource {
    override suspend fun add(userRole: UserRole) {
        deleteAll()
        save(UserRoleEntity(role = userRole))
    }

    override suspend fun getUserRole(): UserRole {
        val userRoleEntity = getAll()
        return userRoleEntity.role
    }

    @Insert
    suspend fun save(userRoleEntity: UserRoleEntity)

    @Query("SELECT * FROM UserRoleEntity")
    suspend fun getAll(): UserRoleEntity

    @Query("DELETE FROM UserRoleEntity")
    suspend fun deleteAll()
}