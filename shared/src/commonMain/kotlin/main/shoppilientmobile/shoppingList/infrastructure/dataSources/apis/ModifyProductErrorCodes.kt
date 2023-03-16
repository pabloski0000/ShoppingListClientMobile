package main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis

enum class ModifyProductErrorCodes(val code: Int) {
    THERE_IS_ANOTHER_PRODUCT_WITH_THAT_NAME(201),
    PRODUCT_EXCEEDS_MAXIMUM_LENGTH(202),
    PRODUCT_IS_SHORTER_THAN_MINIMUM_LENGTH(203),
    PRODUCT_DOES_NOT_EXIST(204);
}