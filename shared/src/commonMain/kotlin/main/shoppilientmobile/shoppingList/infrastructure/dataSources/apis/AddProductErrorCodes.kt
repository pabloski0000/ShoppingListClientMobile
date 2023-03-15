package main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis

enum class AddProductErrorCodes(val code: Int) {
    PRODUCT_ALREADY_EXISTS_ON_LIST(101),
    PRODUCT_EXCEEDS_MAXIMUM_LENGTH(102),
    PRODUCT_IS_SHORTER_THAN_MINIMUM_LENGTH(103),
}