package main.shoppilientmobile.domain.exceptions

class UserDoesNotExistException(
    override val message: String,
) : RuntimeException()