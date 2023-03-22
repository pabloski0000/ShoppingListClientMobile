package main.shoppilientmobile.android.userRegistrationFeature.infrastructure.doubles

import main.shoppilientmobile.shoppingList.application.UserRegistrationsListener
import main.shoppilientmobile.userRegistrationFeature.entities.Registration

class UserRegistrationListenerMock : UserRegistrationsListener {
    var lastComingRegistration: Registration? = null

    override fun userRegistered(registration: Registration) {
        lastComingRegistration = registration
    }
}