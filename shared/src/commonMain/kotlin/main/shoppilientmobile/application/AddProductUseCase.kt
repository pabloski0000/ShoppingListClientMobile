package main.shoppilientmobile.application

import main.shoppilientmobile.application.exceptions.InconsistencyBetweenLocalAndRepositoryException
import main.shoppilientmobile.application.exceptions.ProductCouldNotBeAddedException
import main.shoppilientmobile.application.repositories.ShoppingListRepository
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ShoppingList

class AddProductUseCase(
    private val shoppingList: ShoppingList,
    private val shoppingListRepository: ShoppingListRepository
) {
    fun addProduct(product: Product) {
        addProductTransactionally(product)
    }

    private fun addProductTransactionally(product: Product){
        val normalAdditionGoesRight =
            doesShoppingListAdditionGoRight { shoppingList.addProduct(product) }
        val repositoryAdditionGoesRight =
            doesShoppingListAdditionRepositoryGoRight { shoppingListRepository.addProduct(product) }
        if (
            normalAdditionGoesRight != repositoryAdditionGoesRight
        ) {
            if (normalAdditionGoesRight) {
                throwInconsistencyBetweenLocalAndRepositoryExceptionIfGoesWrong {
                    shoppingList.removeProduct(product)
                }
            } else {
                throwInconsistencyBetweenLocalAndRepositoryExceptionIfGoesWrong {
                    shoppingListRepository.removeProduct(product)
                }
            }
            throwException()
        }
    }

    private fun doesShoppingListAdditionGoRight(shoppingListAddition: () -> Unit): Boolean{
        return ! doesItThrowException(shoppingListAddition)
    }

    private fun doesShoppingListAdditionRepositoryGoRight(
        shoppingListRepositoryAddition: () -> Unit
    ): Boolean {
        return ! doesItThrowException(shoppingListRepositoryAddition)
    }

    private fun doesItThrowException(task: () -> Unit): Boolean {
        try {
            task()
        } catch (e: Exception) {
            return true
        }
        return false
    }

    private fun throwInconsistencyBetweenLocalAndRepositoryExceptionIfGoesWrong(
        task: () -> Unit
    ) {
        try {
            task()
        } catch (e: Exception) {
            throw InconsistencyBetweenLocalAndRepositoryException("""
                Inconsistency between local and repository data
            """.trimIndent())
        }
    }

    private fun throwException() {
        throw ProductCouldNotBeAddedException("Product has not been added due to an" +
                " application error")
    }
}