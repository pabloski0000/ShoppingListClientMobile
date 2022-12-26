package main.shoppilientmobile.domain

import CustomHttpClient

enum class Role {
    ADMIN,
    NORMAL
}

class User(private val nickname: String, private val role: Role){
    fun getNickname(): String{
        return nickname
    }
    fun getRole(): Role{
        return when(role){
            Role.ADMIN -> Role.ADMIN
            Role.NORMAL -> Role.NORMAL
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
    val serverApi = CustomHttpClient()
    serverApi.registerUserAsAdmin(user)
}

