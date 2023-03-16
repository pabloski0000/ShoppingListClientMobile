package main.shoppilientmobile.domain.exceptions

class ThereCannotBeTwoUsersWithTheSameNicknameException(
    override val message: String,
) : RuntimeException()