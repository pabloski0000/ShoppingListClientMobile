package main.shoppilientmobile.android.shoppingList

import main.shoppilientmobile.android.shoppingList.data.AndroidShoppingList
import main.shoppilientmobile.android.shoppingList.presentation.AndroidShoppingListUI
import main.shoppilientmobile.android.shoppingList.presentation.ProductFactoryViewModelFactory
import main.shoppilientmobile.android.shoppingList.presentation.ShoppingListViewModelFactory
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.androidRepository.room.RoomDb
import main.shoppilientmobile.android.userRegistrationFeatureAndroid.containers.RegistrationContainer
import main.shoppilientmobile.core.remote.StreamingHttpClient
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.shoppingList.application.ShoppingListSynchroniserUseCase
import main.shoppilientmobile.shoppingList.application.ShoppingListUiListener
import main.shoppilientmobile.shoppingList.domain.ShoppingList
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi
import main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis.ServerShoppingListApi2
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ServerShoppingList

class ShoppingListContainer(
    streamingHttpClient: StreamingHttpClient,
    securityTokenKeeper: SecurityTokenKeeper,
    room: RoomDb,
) {
    private val shoppingListDao = room.shoppingListDao()

    private val shoppingList: ShoppingList = AndroidShoppingList(shoppingListDao)

    private val serverShoppingListApi2 = ServerShoppingListApi2(
        streamingHttpClient,
        securityTokenKeeper,
    )

    private val serverShoppingListApi = ServerShoppingListApi(
        streamingHttpClient,
        securityTokenKeeper,
    )

    private val androidShoppingListUI = AndroidShoppingListUI()

    private val serverShoppingList = ServerShoppingList(serverShoppingListApi2)

    val shoppingListSynchroniserUseCase = ShoppingListSynchroniserUseCase(
        serverShoppingListApi,
        shoppingList,
    )


    private val shoppingListUiListener = ShoppingListUiListener(
        androidShoppingListUI,
        serverShoppingList,
    )

    val shoppingListViewModelFactory = ShoppingListViewModelFactory(shoppingList, androidShoppingListUI)

    val productFactoryViewModelFactory = ProductFactoryViewModelFactory(
        shoppingList,
        androidShoppingListUI,
    )
}