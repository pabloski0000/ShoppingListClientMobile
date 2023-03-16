package main.shoppilientmobile.domain.exceptions

class ThereCanOnlyBeOneAdmin(
    override val message: String,
) : RuntimeException()