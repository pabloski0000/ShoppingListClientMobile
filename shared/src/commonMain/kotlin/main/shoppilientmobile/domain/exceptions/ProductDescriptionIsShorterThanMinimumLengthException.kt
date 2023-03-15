package main.shoppilientmobile.domain.exceptions

class ProductDescriptionIsShorterThanMinimumLengthException(
    override val message: String,
) : RuntimeException() {
}