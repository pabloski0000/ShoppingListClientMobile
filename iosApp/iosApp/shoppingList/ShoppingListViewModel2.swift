//
//  UserRegistrationViewModel.swift
//  iosApp
//
//  Created by Pablo Mendez Jimenez on 4/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension ShoppingListScreen {
    class ShoppingListViewModel2: ObservableObject, SharedShoppingListObserver {
        private let iosContainer = IosContainer.getIosContainer()
        private let synchroniseWithSharedShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase
        @Published private(set) var listOfProducts = [Product]()
        
        init() {
            synchroniseWithSharedShoppingListUseCase = iosContainer.synchroniseWithRemoteShoppingListUseCase
        }
        
        func loadSharedShoppingAndNotifyChanges() {
            synchroniseWithSharedShoppingListUseCase.synchronise(observer: self)
        }
        
        func currentState(products: [Product]) {
            listOfProducts.removeAll()
            listOfProducts.append(contentsOf: products)
        }
        
        func productAdded(product: Product) {
            listOfProducts.append(product)
        }
        
        func productDeleted(product: Product) {
            let indexOfProductToDelete = listOfProducts.firstIndex(of: product)!
            listOfProducts.remove(at: indexOfProductToDelete)
        }
        
        func productModified(oldProduct: Product, newProduct: Product) {
            let indexOfProductToModify = listOfProducts.firstIndex(of: oldProduct)!
            listOfProducts[indexOfProductToModify] = newProduct
        }
    }
}
