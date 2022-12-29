package main.shoppilientmobile.android

import android.app.Application
import main.shoppilientmobile.android.core.AndroidContainer

class AndroidApplication: Application() {
    val androidContainer = AndroidContainer()
}