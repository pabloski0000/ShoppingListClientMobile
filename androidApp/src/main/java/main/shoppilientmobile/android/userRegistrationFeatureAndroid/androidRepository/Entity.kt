package main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository

import androidx.room.Entity
import main.shoppilientmobile.domain.domainExposure.UserRole

sealed class Entity {
    @Entity
    data class UserRoleEntity(val userRole: UserRole)
}
