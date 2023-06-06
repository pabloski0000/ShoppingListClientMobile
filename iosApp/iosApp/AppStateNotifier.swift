//
//  AppStateNotifier.swift
//  iosApp
//
//  Created by Pablo Mendez Jimenez on 5/6/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class AppStateNotifier {
    private let synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase
    
    init(synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase) {
        self.synchroniseWithRemoteShoppingListUseCase = synchroniseWithRemoteShoppingListUseCase
        NotificationCenter.default.addObserver(self, selector: #selector(appWillEnterTheForeground), name: UIApplication.willEnterForegroundNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(appDidEnterBackground), name: UIApplication.didEnterBackgroundNotification, object: nil)
    }
    
    @objc func appWillEnterTheForeground() {
        synchroniseWithRemoteShoppingListUseCase.continueSynchronisation()
    }
    
    @objc func appDidEnterBackground() {
        synchroniseWithRemoteShoppingListUseCase.stopSynchronisation(completionHandler: {error in })
    }
}
