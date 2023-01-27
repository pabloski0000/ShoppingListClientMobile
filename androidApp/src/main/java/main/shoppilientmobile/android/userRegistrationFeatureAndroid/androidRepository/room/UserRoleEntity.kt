package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import main.shoppilientmobile.domain.domainExposure.UserRole

@Entity
data class UserRoleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: UserRole,
)