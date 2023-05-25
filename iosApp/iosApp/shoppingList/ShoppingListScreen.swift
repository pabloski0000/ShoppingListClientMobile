import SwiftUI
import KMMViewModelSwiftUI
import shared

struct ShoppingListScreen: View {
    @StateViewModel var viewModel = IosContainer.getIosContainer().shoppingListViewModelShared
    @State var navigationLinkActive: Bool = true
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                NavigationLink(destination: ProductShapperScreen(productOnScreen: "", mode: ProductShapperScreen.Mode.Create)) {
                    Image(systemName: "plus")
                }
            }
            .frame(maxWidth: .infinity, minHeight: 20)
            .padding()
            List {
                Text("Lista de la compra")
                ForEach(viewModel.productItemsUiState, id: \.content) { product in
                    HStack {
                        NavigationLink(destination: ProductShapperScreen(productOnScreen: product.content, mode: ProductShapperScreen.Mode.Modify)) {
                            Text(product.content)
                            EmptyView()
                        }
                        Image(systemName: "xmark")
                            .onTapGesture {
                                viewModel.deleteProducts(products: [product.content], completionHandler: {error in })
                            }
                    }
                }
            }.onAppear() {
                viewModel.loadSharedShoppingAndNotifyChanges()
            }
        }
    }
    
    func navigateToProductShaperScreen(initialText: String) {
        
    }
}

struct ScreenRegistrationScreen_Previews: PreviewProvider {
	static var previews: some View {
		ShoppingListScreen()
	}
}
