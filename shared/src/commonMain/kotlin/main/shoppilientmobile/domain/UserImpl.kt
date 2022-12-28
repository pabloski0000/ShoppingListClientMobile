package main.shoppilientmobile.domain

import ServerRequests
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole

class UserImpl(private var nickname: String, private val role: UserRole): User{
    override fun getId(): String {
        TODO("Not yet implemented")
    }

    override fun changeNickname(newNickname: String) {
        nickname = newNickname
    }

    override fun getNickname(): String{
        return nickname
    }

    override fun getRole(): UserRole{
        return when(role){
            UserRole.ADMIN -> UserRole.ADMIN
            UserRole.BASIC -> UserRole.BASIC
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
    val serverApi = ServerRequests()
    serverApi.registerAdminUser(user)
}
