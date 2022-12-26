package main.shoppilientmobile.application

import main.shoppilientmobile.application.applicationExposure.UserBuilder
import main.shoppilientmobile.domain.UserImpl
import main.shoppilientmobile.domain.domainExposure.User

class UserBuilderImpl: UserBuilder {
    private var nickname = "default"
    private var role = User.Role.BASIC

    override fun giveItANickname(userNickname: String): UserBuilder {
        nickname = userNickname
        return this
    }

    override fun setRole(userRole: User.Role): UserBuilder {
        role = userRole
        return this
    }

    override fun build(): User {
        return UserImpl(nickname, role)
    }

}