package main.shoppilientmobile.repositories

import main.shoppilientmobile.ApiServer
import main.shoppilientmobile.application.repositories.UserRepository
import main.shoppilientmobile.domain.Role
import main.shoppilientmobile.domain.User

class UserRepositoryImpl(
    private val apiServer: ApiServer
): UserRepository {

    override fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun addUser(user: User) {
        when(user.getRole()) {
            Role.BASIC -> apiServer.registerBasicUser(user)
            Role.ADMIN -> apiServer.registerAdminUser(user)
        }
    }

    override fun modifyUser(oldUser: User, newUser: User) {
        TODO("Not yet implemented")
    }

    override fun removeUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun contains(user: User): Boolean {
        TODO("Not yet implemented")
    }


}