//
//  AddProductScreen.swift
//  iosApp
//
//  Created by Pablo Mendez Jimenez on 12/5/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct AddProductScreen: View {
    @StateObject private var viewModel: AddProductViewModel = AddProductViewModel()
    @State var product: String = ""
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        TextField("Escribe el producto para añadir", text: $product) {
            viewModel.addProduct(product: $product.wrappedValue)
            if (viewModel.requestState == AddProductViewModel.RequestState.FINISHED_SUCCESSFULLY) {
                presentationMode.wrappedValue.dismiss()
            }
        }
        Text(viewModel.errorMessage)
    }
}

struct AddProductScreen_Previews: PreviewProvider {
    static var previews: some View {
        AddProductScreen()
    }
}
