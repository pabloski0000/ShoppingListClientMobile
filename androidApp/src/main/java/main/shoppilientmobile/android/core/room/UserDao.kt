package main.shoppilientmobile.android.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun save(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE nickname = :nickname")
    suspend fun getUser(nickname: String): UserEntity

    @Query("SELECT * FROM UserEntity WHERE isLocal = 1")
    suspend fun getLocalUser(): UserEntity
}