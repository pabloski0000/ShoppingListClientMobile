package main.shoppilientmobile.userRegistrationFeature.useCases.exceptions

class WrongCodeException(
    override val message: String
) : RuntimeException() {
}