package main.shoppilientmobile.shoppingList.application.doubles

import main.shoppilientmobile.shoppingList.application.AddProductUseCase2Ui

class AddProductUseCase2UiSpy : AddProductUseCase2Ui {
    var hasInformedUserOfDuplicateProduct = false

    override fun informUserOfProductDuplication() {
        hasInformedUserOfDuplicateProduct = true
    }
}