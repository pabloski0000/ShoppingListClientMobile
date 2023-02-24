package main.shoppilientmobile.android.core.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val nickname: String,
    val role: String,
    val isLocal: Boolean,
)
