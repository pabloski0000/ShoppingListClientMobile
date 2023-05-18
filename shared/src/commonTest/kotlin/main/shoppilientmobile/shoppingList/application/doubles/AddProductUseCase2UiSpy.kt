package main.shoppilientmobile.shoppingList.application.doubles

class AddProductUseCase2UiSpy : AddProductUseCase2Ui {
    var hasInformedUserOfDuplicateProduct = false

    override fun informUserOfProductDuplication() {
        hasInformedUserOfDuplicateProduct = true
    }
}