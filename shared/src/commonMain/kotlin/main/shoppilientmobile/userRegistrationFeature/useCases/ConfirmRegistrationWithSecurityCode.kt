package main.shoppilientmobile.userRegistrationFeature.useCases

typealias confirmRegistrationWithSecurityCode = suspend (code: String) -> Unit
