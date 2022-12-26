package main.shoppilientmobile.application.applicationExposure.repositories

import main.shoppilientmobile.domain.domainExposure.User


interface UserRepository {
    fun getUsers(): List<User>
    fun addUser(user: User)
    fun modifyUser(oldUser: User, newUser: User)
    fun removeUser(user: User)
    fun contains(user: User): Boolean
}