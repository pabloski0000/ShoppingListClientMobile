package main.shoppilientmobile.userRegistrationFeature.dataSources

import main.shoppilientmobile.userRegistrationFeature.entities.Registration

interface NewUserRegistrationRemoteDataSource {
    suspend fun confirmUserRegistration(registration: Registration)
}