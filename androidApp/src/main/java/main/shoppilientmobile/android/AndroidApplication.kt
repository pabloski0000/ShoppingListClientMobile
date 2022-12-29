package main.shoppilientmobile.android

import android.app.Application
import main.shoppilientmobile.android.androidApplication.containers.UserRegistrationContainer

class AndroidApplication: Application() {
    val userRegistrationContainer = UserRegistrationContainer()
}