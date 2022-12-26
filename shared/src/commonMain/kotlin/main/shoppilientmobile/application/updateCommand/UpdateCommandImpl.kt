package main.shoppilientmobile.application.updateCommand

import main.shoppilientmobile.application.applicationExposure.exceptions.InconsistencyBetweenLocalAndRepositoryException

class UpdateCommandImpl(
    private val domainUpdate: () -> Unit,
    private val repositoryUpdate: () -> Unit,
    private val domainUpdateRollback: () -> Unit,
    private val repositoryUpdateRollback: () -> Unit,
): UpdateCommand {
    override fun execute() {
        if (doesDomainUpdateGoWrong(domainUpdate)) {
            throwDataInconsistencyExceptionIfRollbackFails { domainUpdateRollback() }
            throwExceptionDueToViolationOfDomainRules()
        }
        if (doesRepositoryUpdateGoWrong(repositoryUpdate)) {
            domainUpdateRollback()
            throwDataInconsistencyExceptionIfRollbackFails { repositoryUpdateRollback() }
            throwExceptionToInformOfRepositoryException()
        }
    }

    private fun doesDomainUpdateGoWrong(shoppingListAddition: () -> Unit): Boolean{
        return doesItThrowException(shoppingListAddition)
    }

    private fun doesRepositoryUpdateGoWrong(shoppingListRepositoryAddition: () -> Unit): Boolean {
        return doesItThrowException(shoppingListRepositoryAddition)
    }

    private fun doesItThrowException(task: () -> Unit): Boolean {
        try {
            task()
        } catch (e: Exception) {
            return true
        }
        return false
    }

    private fun throwDataInconsistencyExceptionIfRollbackFails(task: () -> Unit) {
        try {
            task()
        } catch (e: Exception) {
            throw InconsistencyBetweenLocalAndRepositoryException("""
                Inconsistency between local and repository data
            """.trimIndent())
        }
    }

    private fun throwExceptionDueToViolationOfDomainRules() {
        throw RuntimeException("Domain could not be updated due to a violation of its rules")
    }

    private fun throwExceptionToInformOfRepositoryException() {
        throw RuntimeException("Repository could not be updated")
    }
}