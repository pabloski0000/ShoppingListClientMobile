package main.shoppilientmobile.shoppingList.application

interface BusinessRulesExceptionsListener {
    fun informClientOfTheBusinessRuleDisobeyed(explanation: String)
}