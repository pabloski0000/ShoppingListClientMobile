package main.shoppilientmobile.android.core

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import main.shoppilientmobile.android.shoppingList.data.AndroidShoppingList
import main.shoppilientmobile.shoppingList.domain.ShoppingList
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.KeyValueLocalStorage
import main.shoppilientmobile.android.core.room.RoomDb
import main.shoppilientmobile.android.shoppingList.presentation.*
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.core.remote.AsynchronousHttpClientImpl
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.SecurityTokenKeeperImpl
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.UserLocalDataSourceAndroid
import main.shoppilientmobile.core.remote.UserApi
import main.shoppilientmobile.core.remote.dataSources.UserRemoteDataSource
import main.shoppilientmobile.dataSources.StreamingHttpClientAndroid
import main.shoppilientmobile.shoppingList.application.*
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ServerShoppingList
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.RegistrationApi
import main.shoppilientmobile.userRegistrationFeature.repositories.RegistrationRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.repositories.UserRepositoryImpl
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.GetLocalUserUseCase

class AndroidContainer(
    private val context: Context,
) {
    val dataStore = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("preferences")
        }
    )

    val room = Room.databaseBuilder(context, RoomDb::class.java, "room_db").fallbackToDestructiveMigration()
        .build()

    private val shoppingListDao = room.shoppingListDao()

    private val userDao = room.getUserDao()

    private val shoppingList: ShoppingList = AndroidShoppingList(shoppingListDao)

    val httpClient = AsynchronousHttpClientImpl()

    val streamingHttpClient = StreamingHttpClientAndroid()

    private val keyValueLocalStorage = KeyValueLocalStorage(
        dataStore = dataStore
    )

    val securityTokenKeeper = SecurityTokenKeeperImpl(
        keyValueLocalStorage = keyValueLocalStorage,
    )

    private val userApi = UserApi(
        httpClient,
        securityTokenKeeper,
    )

    private val serverShoppingListApi = ServerShoppingListApi(
        streamingHttpClient,
        securityTokenKeeper,
    )

    private val registrationApi = RegistrationApi(
        httpClient = httpClient,
        securityTokenKeeper = securityTokenKeeper,
        streamingHttpClient = streamingHttpClient,
    )

    private val userRemoteDataSource = UserRemoteDataSource(
        userApi,
    )

    private val userLocalDataSourceAndroid = UserLocalDataSourceAndroid(
        userDao,
    )

    val userRepository = UserRepositoryImpl(
        userRemoteDataSource,
        userLocalDataSourceAndroid,
    )

    val registrationRepository = RegistrationRepositoryImpl(
        registrationApi,
    )

    val remoteShoppingList: RemoteShoppingList = ServerShoppingList(serverShoppingListApi)

    private val addProductUseCase = AddProductUseCase(
        remoteShoppingList,
    )

    private val modifyProductUseCase = ModifyProductUseCase(
        remoteShoppingList,
    )

    private val deleteProductUseCase = DeleteProductUseCase(
        remoteShoppingList,
    )

    val getLocalUserUseCase = GetLocalUserUseCase(
        userRepository,
    )

    private val synchroniseWithRemoteShoppingListUseCase = SynchroniseWithRemoteShoppingListUseCase(
        remoteShoppingList
    )

    val shoppingListSynchroniserUseCase = ShoppingListSynchroniserUseCase(
        remoteShoppingList,
        shoppingList,
    )

    private val listenToUserRegistrationsUseCase = ListenToUserRegistrationsUseCase(
        userRepository,
        registrationRepository,
    )

    val androidShoppingListUI = AndroidShoppingListUI(
        addProductUseCase,
        modifyProductUseCase,
        deleteProductUseCase,
        synchroniseWithRemoteShoppingListUseCase,
        getLocalUserUseCase,
        listenToUserRegistrationsUseCase,
    )

    var registrationContainer: RegistrationContainer? = null

    val shoppingModeViewModel = ShoppingModeViewModel(
        androidShoppingListUI,
    )

    val userRegistrationsViewModel = UserRegistrationsViewModel(
        androidShoppingListUI,
    )

    val shoppingListViewModelFactory = ShoppingListViewModelFactory(shoppingList, androidShoppingListUI)

    val productFactoryViewModelFactory = ProductFactoryViewModelFactory(
        shoppingList,
        androidShoppingListUI,
    )
}