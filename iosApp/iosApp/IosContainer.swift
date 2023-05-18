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
    
    init() {
        nonBlockingHttpClient = NonBlockingHttpClientImpl(httpClientEngineFactory: HttpClientEngineFactoryBuilder().buildDarwinEngineFactory())
        serverShoppingListApi = ServerShoppingListApi(streamingHttpClient: nonBlockingHttpClient, nonBlockingHttpClient: nonBlockingHttpClient)
        serverShoppingList = ServerShoppingList(serverShoppingListApi: serverShoppingListApi)
        synchroniseWithRemoteShoppingListUseCase = SynchroniseWithRemoteShoppingListUseCase(remoteShoppingList: serverShoppingList)
        addProductUseCase = AddProductUseCase(remoteShoppingList: serverShoppingList)
    }
    
    static func getIosContainer() -> IosContainer {
        if (singleton == nil) {
            singleton = IosContainer()
        }
        return singleton!
    }
    
    
}
