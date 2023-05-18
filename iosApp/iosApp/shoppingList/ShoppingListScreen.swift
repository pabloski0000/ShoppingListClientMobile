import SwiftUI
import shared

struct ShoppingListScreen: View {
    @StateObject private var viewModel = ShoppingListViewModel()
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                NavigationLink(destination: AddProductScreen()) {
                    Image(systemName: "plus")
                }
            }
            .frame(maxWidth: .infinity, minHeight: 20)
            .padding()
            List {
                Text("List of products")
                ForEach(viewModel.listOfProducts, id: \.description_) { product in
                    Text(product.description())
                }
            }.onAppear() {
                viewModel.loadSharedShoppingAndNotifyChanges()
            }
        }
    }
}

struct ScreenRegistrationScreen_Previews: PreviewProvider {
	static var previews: some View {
		ShoppingListScreen()
	}
}
