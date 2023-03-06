package main.shoppilientmobile.android.shoppingList.presentation

data class CheckableProductState(val checked: Boolean, val productDescription: String) {
    companion object {
        fun fromProduct(product: Product): CheckableProductState {
            return CheckableProductState(false, product.description)
        }
    }

    fun toProduct(): Product {
        return Product(productDescription)
    }
}