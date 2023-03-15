package main.shoppilientmobile.domain.exceptions

class ProductDescriptionExceedsMaximumLengthException(
    override val message: String,
) : RuntimeException() {
}