package main.shoppilientmobile.application.repositories

import main.shoppilientmobile.domain.User

interface UserRepository {
    fun getUsers(): List<User>
    fun addUser(user: User)
    fun modifyUser(oldUser: User, newUser: User)
    fun removeUser(user: User)
    fun contains(user: User): Boolean
}