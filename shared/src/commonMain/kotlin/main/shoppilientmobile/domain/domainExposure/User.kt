package main.shoppilientmobile.domain.domainExposure

interface User {
    fun getId(): String
    fun getNickname(): String
    fun getRole(): UserRole
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}