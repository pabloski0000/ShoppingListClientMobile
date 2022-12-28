package main.shoppilientmobile.application.applicationExposure

import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole

interface UserBuilder {
    fun giveItANickname(userNickname: String): UserBuilder
    fun setRole(role: UserRole): UserBuilder
    fun build(): User
}