package main.shoppilientmobile.unitTests.application

import main.shoppilientmobile.domain.Product
import kotlin.test.assertEquals

class SharedShoppingListSynchronizationTest {

    /*private fun assertProductIsInLocalSharedShoppingList(productToAdd: Product){
        assertEquals(productToAdd, localShoppingListMock.getProducts().first())
    }

    @Test
    fun followsUseCaseCorrectly(){
        val productToAdd = buildProduct()
        localProductAddition.addProduct(productToAdd)
        assertProductIsInShoppingList(productToAdd)
        assertProductIsInShoppingListRepository(productToAdd)
    }

    private fun assertProductIsInShoppingList(productToAdd: Product){
        assertEquals(productToAdd, localShoppingListMock.getProducts().first())
    }

    private fun assertProductIsInShoppingListRepository(productToAdd: Product){
        assertEquals(productToAdd, shoppingListRepositoryMock.getProducts().first())
    }

    @Test
    fun maintainDataConsistencyWhenRepositoryFails(){
        val productToAdd = buildProduct()
        shoppingListRepositoryMock.throwExceptionOnNextMethodCall()
        try {
            localProductAddition.addProduct(productToAdd)
        } catch (e: Exception) {

        }
        assertShoppingListAndItsRepositoryHasTheSameData()
    }

    private fun assertShoppingListAndItsRepositoryHasTheSameData(){
        assertEquals(localShoppingListMock.getProducts(), shoppingListRepositoryMock.getProducts())
    }

    @Test
    fun maintainDataConsistencyWhenProductIsNotAddedOnLocalShoppingList(){
        val productToAdd = buildProduct()
        localShoppingListMock.throwExceptionOnNextMethodCall()
        try {
            localProductAddition.addProduct(productToAdd)
        } catch (e: Exception) {

        }
        assertShoppingListAndItsRepositoryHasTheSameData()
    }

    private fun buildProduct() = Product("description")*/
}