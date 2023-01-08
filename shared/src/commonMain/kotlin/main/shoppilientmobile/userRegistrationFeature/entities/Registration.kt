package main.shoppilientmobile.userRegistrationFeature.entities

import main.shoppilientmobile.domain.domainExposure.UserRole

data class Registration(val nickname: String, val role: UserRole, val signature: String? = null)
