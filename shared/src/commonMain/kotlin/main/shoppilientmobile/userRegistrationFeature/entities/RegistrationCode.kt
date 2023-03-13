package main.shoppilientmobile.userRegistrationFeature.entities

data class RegistrationCode(val value: Int) {
    private val maxValue = 10000000
    private val minValue = 0
    init {
        if (value !in minValue..maxValue) {
            throw InvalidValueObjectState("Code is $value but it must be" +
                    " lower or equal to $maxValue and greater or equal to $minValue")
        }
    }
}