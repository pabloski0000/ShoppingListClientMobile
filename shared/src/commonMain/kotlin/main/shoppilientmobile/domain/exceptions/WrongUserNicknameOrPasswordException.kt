package main.shoppilientmobile.domain.exceptions

class WrongUserNicknameOrPasswordException(
    override val message: String,
) : RuntimeException()