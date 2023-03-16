package main.shoppilientmobile.domain.exceptions

class UserNicknameTooShortException(
    override val message: String,
) : RuntimeException()