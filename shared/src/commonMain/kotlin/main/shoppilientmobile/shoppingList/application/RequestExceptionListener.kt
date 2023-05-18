package main.shoppilientmobile.shoppingList.application

interface RequestExceptionListener {
    fun informUserOfError(explanation: String)
}