package main.shoppilientmobile.domain

import ServerRequests
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole

class UserImpl(private val nickname: String, private val role: UserRole): User{
    private val minimumNicknameLength = 3
    private val maximumNicknameLength = 20

    init {
        if (nickname.length !in minimumNicknameLength..maximumNicknameLength)
            throw RuntimeException("User nickname needs to be between" +
                    " $minimumNicknameLength and $maximumNicknameLength characters")
    }

    override fun getId(): String {
        TODO("Not yet implemented")
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

