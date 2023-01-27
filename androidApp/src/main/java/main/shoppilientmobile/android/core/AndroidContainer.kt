package main.shoppilientmobile.android.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import main.shoppilientmobile.android.shoppingList.data.AndroidShoppingList
import main.shoppilientmobile.android.shoppingList.domain.ShoppingList
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.KeyValueLocalStorage
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.room.RoomDb
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.core.remote.AsynchronousHttpClientImpl
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.SecurityTokenKeeperImpl
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.UserLocalDataSourceAndroid
import main.shoppilientmobile.dataSources.StreamingHttpClientAndroid
import main.shoppilientmobile.userRegistrationFeature.dataSources.UserLocalDataSource
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApiWithoutKtor
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl

class AndroidContainer(
    private val context: Context,
) {
    val dataStore = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("preferences")
        }
    )

    val room = Room.databaseBuilder(context, RoomDb::class.java, "room_db")
        .fallbackToDestructiveMigration()
        .build()

    private val shoppingListDao = room.shoppingListDao()

    val shoppingList: ShoppingList = AndroidShoppingList(shoppingListDao)

    val httpClient = AsynchronousHttpClientImpl()

    private val streamingHttpClient = StreamingHttpClientAndroid()

    private val keyValueLocalStorage = KeyValueLocalStorage(
        dataStore = dataStore
    )

    val securityTokenKeeper = SecurityTokenKeeperImpl(
        keyValueLocalStorage = keyValueLocalStorage,
    )

    private val userApi = UserApiWithoutKtor(
        httpClient = httpClient,
        streamingHttpClient = streamingHttpClient,
        securityTokenKeeper = securityTokenKeeper
    )

    val userRepository = UserRepositoryImpl(
        UserLocalDataSourceAndroid(),
    )

    var registrationContainer: RegistrationContainer? = null
}