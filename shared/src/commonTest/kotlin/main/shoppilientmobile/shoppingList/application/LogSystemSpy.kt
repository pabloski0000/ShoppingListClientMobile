package main.shoppilientmobile.shoppingList.application

class LogSystemSpy : LogSystem {
    val infoMessages = mutableListOf<String>()
    val warningMessages = mutableListOf<String>()
    val errorMessages = mutableListOf<String>()

    override fun infoMessage(message: String) {
        infoMessages.add(message)
    }

    override fun warningMessage(message: String) {
        warningMessages.add(message)
    }

    fun clear() {
        infoMessages.clear()
        warningMessages.clear()
        errorMessages.clear()
    }
}