package main.shoppilientmobile.domain.domainExposure

interface User {
    enum class Role {
        ADMIN,
        BASIC
    }
    fun getId(): String
    fun changeNickname(newNickname: String)
    fun getNickname(): String
    fun getRole(): Role
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}