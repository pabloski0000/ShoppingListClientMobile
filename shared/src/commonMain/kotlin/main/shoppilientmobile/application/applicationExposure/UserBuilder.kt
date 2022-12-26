package main.shoppilientmobile.application.applicationExposure

import main.shoppilientmobile.domain.domainExposure.User

interface UserBuilder {
    fun giveItANickname(userNickname: String): UserBuilder
    fun setRole(role: User.Role): UserBuilder
    fun build(): User
}