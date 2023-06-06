//
//  IosContainer.swift
//  iosApp
//
//  Created by Pablo Mendez Jimenez on 4/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class IosContainer {
    private static var singleton: IosContainer? = nil
    private let nonBlockingHttpClient: NonBlockingHttpClientImpl
    private let serverShoppingListApi: ServerShoppingListApi
    private let serverShoppingList: ServerShoppingList
    let synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase
    let addProductUseCase: AddProductUseCase
    let modifyProductUseCase: ModifyProductUseCase
    let deleteProductUseCase: DeleteProductUseCase
    let shoppingListViewModelShared: ShoppingListViewModelShared
    private let appStateNotifier: AppStateNotifier
    
    init() {
        nonBlockingHttpClient = NonBlockingHttpClientImpl(httpClientEngineFactory: HttpClientEngineFactoryBuilder().buildDarwinEngineFactory())
        serverShoppingListApi = ServerShoppingListApi(streamingHttpClient: nonBlockingHttpClient, nonBlockingHttpClient: nonBlockingHttpClient)
        serverShoppingList = ServerShoppingList(serverShoppingListApi: serverShoppingListApi)
        synchroniseWithRemoteShoppingListUseCase = SynchroniseWithRemoteShoppingListUseCase(remoteShoppingList: serverShoppingList)
        addProductUseCase = AddProductUseCase(remoteShoppingList: serverShoppingList)
        modifyProductUseCase = ModifyProductUseCase(remoteShoppingList: serverShoppingList)
        deleteProductUseCase = DeleteProductUseCase(remoteShoppingList: serverShoppingList)
        shoppingListViewModelShared = ShoppingListViewModelShared(
            synchroniseWithRemoteShoppingListUseCase: synchroniseWithRemoteShoppingListUseCase,
            addProductUseCase: addProductUseCase,
            modifyProductUseCase: modifyProductUseCase,
            deleteProductUseCase: deleteProductUseCase,
            getLocalUserUseCase: nil
        )
        appStateNotifier = AppStateNotifier(synchroniseWithRemoteShoppingListUseCase: synchroniseWithRemoteShoppingListUseCase)
    }
    
    static func getIosContainer() -> IosContainer {
        if (singleton == nil) {
            singleton = IosContainer()
        }
        return singleton!
    }
    
    
}
