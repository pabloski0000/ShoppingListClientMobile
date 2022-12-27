package main.shoppilientmobile.domain.domainExposure

import main.shoppilientmobile.domain.observableEntities.observablePattern.SharedShoppingListObserver
import main.shoppilientmobile.domain.sharedShoppingList.ProductAdditionObserver

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