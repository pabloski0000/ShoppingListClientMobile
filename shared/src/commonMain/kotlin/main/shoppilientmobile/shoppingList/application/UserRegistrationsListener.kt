package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.userRegistrationFeature.entities.Registration

interface UserRegistrationsListener {
    fun userRegistered(registration: Registration)
}