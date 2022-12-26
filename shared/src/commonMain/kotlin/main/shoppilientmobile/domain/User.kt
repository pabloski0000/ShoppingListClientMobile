package main.shoppilientmobile.domain

import ApiServerImpl

enum class Role {
    ADMIN,
    BASIC
}

class User(private val nickname: String, private val role: Role){
    fun getNickname(): String{
        return nickname
    }
    fun getRole(): Role{
        return when(role){
            Role.ADMIN -> Role.ADMIN
            Role.BASIC -> Role.BASIC
        }
    }
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other::class != User::class) return false
        other as User
        if (nickname != other.nickname) return false

        return true
    }

    override fun hashCode(): Int {
        return nickname.hashCode()
    }
}

fun registerUserToList(user: User){
    val serverApi = ApiServerImpl()
    serverApi.registerAdminUser(user)
}

