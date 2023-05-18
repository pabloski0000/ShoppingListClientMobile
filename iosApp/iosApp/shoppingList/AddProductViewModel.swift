//
//  AddProductViewModel.swift
//  iosApp
//
//  Created by Pablo Mendez Jimenez on 12/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension AddProductScreen {
    class AddProductViewModel: ObservableObject, RequestExceptionListener {
        private let addProductUseCase: AddProductUseCase = IosContainer.getIosContainer().addProductUseCase
        @Published private(set) var requestState: RequestState? = nil
        @Published private(set) var errorMessage: String = ""
        enum RequestState {
            case INITIALISED
            case FINISHED_WITH_ERROR
            case FINISHED_SUCCESSFULLY
        }
        
        func addProduct(product: String) {
            requestState = RequestState.INITIALISED
            let productToAdd = Product(description: product)
            addProductUseCase.addProduct(product: productToAdd, exceptionListener: self, completionHandler: {_ in})
            if (requestState != RequestState.FINISHED_WITH_ERROR) {
                requestState = RequestState.FINISHED_SUCCESSFULLY
                errorMessage = ""
            }
        }
        
        func informUserOfError(explanation: String) {
            requestState = RequestState.FINISHED_WITH_ERROR
            errorMessage = explanation
        }
    }
}
