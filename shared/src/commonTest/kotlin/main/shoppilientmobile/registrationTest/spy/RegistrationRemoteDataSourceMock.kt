package main.shoppilientmobile.registrationTest.spy

import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.shoppingList.application.UserRegistrationsListener
import main.shoppilientmobile.userRegistrationFeature.dataSources.RegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class RegistrationRemoteDataSourceMock : RegistrationRemoteDataSource {

    override suspend fun registerAdmin(registration: Registration): User {
        return UserBuilderImpl().giveItANickname(registration.nickname).setRole(registration.role)
            .build()
    }

    override suspend fun registerUser(registration: Registration) {
        //Does Nothing
    }

    override suspend fun confirmUserRegistration(registration: Registration): User {
        return UserBuilderImpl()
            .giveItANickname(registration.nickname)
            .setRole(registration.role)
            .build()
    }

    override suspend fun listenToUserRegistrations(userRegistrationListener: UserRegistrationsListener) {
        TODO("Not yet implemented")
    }
}