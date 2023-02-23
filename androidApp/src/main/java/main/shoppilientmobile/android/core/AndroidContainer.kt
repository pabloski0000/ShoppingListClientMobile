package main.shoppilientmobile.android.core

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import main.shoppilientmobile.android.shoppingList.data.AndroidShoppingList
import main.shoppilientmobile.android.shoppingList.presentation.AndroidShoppingListUI
import main.shoppilientmobile.shoppingList.domain.ShoppingList
import main.shoppilientmobile.android.shoppingList.presentation.ProductFactoryViewModelFactory
import main.shoppilientmobile.android.shoppingList.presentation.ShoppingListViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.KeyValueLocalStorage
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.room.RoomDb
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.core.remote.AsynchronousHttpClientImpl
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.SecurityTokenKeeperImpl
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.UserLocalDataSourceAndroid
import main.shoppilientmobile.dataSources.StreamingHttpClientAndroid
import main.shoppilientmobile.shoppingList.application.*
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi2
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ServerShoppingList
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
        .build()

    private val shoppingListDao = room.shoppingListDao()

    private val shoppingList: ShoppingList = AndroidShoppingList(shoppingListDao)

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

    private val serverShoppingListApi2 = ServerShoppingListApi2(
        streamingHttpClient,
        securityTokenKeeper,
    )

    private val serverShoppingListApi = ServerShoppingListApi(
        streamingHttpClient,
        securityTokenKeeper,
    )

    val remoteShoppingList: RemoteShoppingList = ServerShoppingList(serverShoppingListApi2)

    private val addProductUseCase = AddProductUseCase(
        remoteShoppingList,
    )

    private val modifyProductUseCase = ModifyProductUseCase(
        remoteShoppingList,
    )

    private val synchroniseWithRemoteShoppingListUseCase = SynchroniseWithRemoteShoppingListUseCase(
        remoteShoppingList
    )

    val androidShoppingListUI = AndroidShoppingListUI(
        addProductUseCase,
        modifyProductUseCase,
        synchroniseWithRemoteShoppingListUseCase,
    )

    val userRepository = UserRepositoryImpl(
        UserLocalDataSourceAndroid(),
    )

    val shoppingListSynchroniserUseCase = ShoppingListSynchroniserUseCase(
        serverShoppingListApi,
        shoppingList,
    )

    var registrationContainer: RegistrationContainer? = null

    val shoppingListViewModelFactory = ShoppingListViewModelFactory(shoppingList, androidShoppingListUI)

    val productFactoryViewModelFactory = ProductFactoryViewModelFactory(
        shoppingList,
        androidShoppingListUI,
    )
}