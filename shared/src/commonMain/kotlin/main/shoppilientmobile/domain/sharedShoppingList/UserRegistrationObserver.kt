package main.shoppilientmobile.domain.sharedShoppingList

import main.shoppilientmobile.domain.domainExposure.User

interface UserRegistrationObserver {
    fun userRegistered(user: User)
}