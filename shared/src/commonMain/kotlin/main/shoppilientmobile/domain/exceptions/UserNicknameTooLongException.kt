package main.shoppilientmobile.domain.exceptions

class UserNicknameTooLongException(
    override val message: String,
) : RuntimeException()