package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.Product

class AddProductUseCase2(
    private val productRepository: ProductRepository,
    private val addProductUseCase2Ui: AddProductUseCase2Ui,
    private val logSystem: LogSystem,
) {

    fun add(product: String) {
        val newProduct = Product(product)
        if (productRepository.exists(newProduct)) {
            addProductUseCase2Ui.informUserOfProductDuplication()
            logSystem.warningMessage("User has tried to add $newProduct - product - but" +
                    " it already exists in shopping list")
            return
        }
        productRepository.add(newProduct)
        logSystem.infoMessage("${this::class.simpleName}. $newProduct product added")
    }

}
