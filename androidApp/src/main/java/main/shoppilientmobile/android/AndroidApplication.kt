package main.shoppilientmobile.android

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.room.RoomDb

class AndroidApplication: Application() {
    var app: App? = null
}