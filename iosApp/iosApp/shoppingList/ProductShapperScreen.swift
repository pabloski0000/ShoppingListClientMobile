//
//  AddProductScreen.swift
//  iosApp
//
//  Created by Pablo Mendez Jimenez on 12/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import KMMViewModelSwiftUI
import shared

struct ProductShapperScreen: View {
    @StateViewModel private var viewModel: ShoppingListViewModelShared = IosContainer.getIosContainer().shoppingListViewModelShared
    @State var productOnScreen: String = ""
    @State private var initialProductState: String = ""
    @State private var errorMessage: String? = nil
    var mode: Mode
    @Environment(\.presentationMode) var presentationMode
    enum Mode {
        case Create
        case Modify
    }
    
    var body: some View {
        TextField("Escribe el producto", text: $productOnScreen) {
            errorMessage = nil
            switch(mode) {
            case Mode.Create:
                viewModel.addProduct(product: $productOnScreen.wrappedValue, response: { errorExplanation in
                    if (errorExplanation == nil) {
                        presentationMode.wrappedValue.dismiss()
                    } else {
                        errorMessage = errorExplanation
                    }
                })
                break
            case Mode.Modify:
                viewModel.modifyProduct(oldProduct: initialProductState, newProduct: $productOnScreen.wrappedValue, response: { errorExplanation in
                    if (errorExplanation == nil) {
                        presentationMode.wrappedValue.dismiss()
                    } else {
                        errorMessage = errorExplanation
                    }
                })
                break
            }
        }
        .font(.title)
        .padding()
        .multilineTextAlignment(TextAlignment.center)
        .onAppear {
            initialProductState = productOnScreen
        }
        Spacer()
        if (errorMessage != nil) {
            Text(errorMessage!)
        }
    }
}

struct AddProductScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductShapperScreen(productOnScreen: "Goodbye", mode: ProductShapperScreen.Mode.Create)
    }
}
