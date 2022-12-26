package main.shoppilientmobile.domain

import ApiServerImpl
import main.shoppilientmobile.domain.domainExposure.User

class UserImpl(private var nickname: String, private val role: User.Role): User{
    override fun getId(): String {
        TODO("Not yet implemented")
    }

    override fun changeNickname(newNickname: String) {
        nickname = newNickname
    }

    override fun getNickname(): String{
        return nickname
    }

    override fun getRole(): User.Role{
        return when(role){
            User.Role.ADMIN -> User.Role.ADMIN
            User.Role.BASIC -> User.Role.BASIC
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other::class != UserImpl::class) return false
        other as UserImpl
        if (nickname != other.nickname) return false

        return true
    }

    override fun hashCode(): Int {
        return nickname.hashCode()
    }
}

fun registerUserToList(user: UserImpl){
    val serverApi = ApiServerImpl()
    serverApi.registerAdminUser(user)
}

