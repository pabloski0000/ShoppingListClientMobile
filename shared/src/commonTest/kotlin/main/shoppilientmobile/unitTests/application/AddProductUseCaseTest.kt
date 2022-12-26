package main.shoppilientmobile.unitTests.application

import main.shoppilientmobile.application.AddProductUseCase
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.unitTests.application.mocks.ShoppingListMock
import main.shoppilientmobile.unitTests.application.mocks.ShoppingListRepositoryMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddProductUseCaseTest {
    private lateinit var shoppingListMock: ShoppingListMock
    private lateinit var shoppingListRepositoryMock: ShoppingListRepositoryMock
    private lateinit var addProductUseCase: AddProductUseCase

    @BeforeTest
    fun setUp(){
        shoppingListMock = ShoppingListMock()
        shoppingListRepositoryMock = ShoppingListRepositoryMock()
        addProductUseCase = AddProductUseCase(shoppingListMock, shoppingListRepositoryMock)
    }

    @Test
    fun followsUseCaseCorrectly(){
        val productToAdd = buildProduct()
        addProductUseCase.addProduct(productToAdd)
        assertProductIsInShoppingList(productToAdd)
        assertProductIsInShoppingListRepository(productToAdd)
    }

    private fun assertProductIsInShoppingList(productToAdd: Product){
        assertEquals(productToAdd, shoppingListMock.getProducts().first())
    }

    private fun assertProductIsInShoppingListRepository(productToAdd: Product){
        assertEquals(productToAdd, shoppingListRepositoryMock.getProducts().first())
    }

    @Test
    fun maintainDataConsistencyWhenRepositoryFails(){
        val productToAdd = buildProduct()
        shoppingListRepositoryMock.throwExceptionOnNextMethodCall()
        try {
            addProductUseCase.addProduct(productToAdd)
        } catch (e: Exception) {

        }
        assertShoppingListAndItsRepositoryHasTheSameData()
    }

    private fun assertShoppingListAndItsRepositoryHasTheSameData(){
        assertEquals(shoppingListMock.getProducts(), shoppingListRepositoryMock.getProducts())
    }

    @Test
    fun maintainDataConsistencyWhenProductIsNotAddedOnLocalShoppingList(){
        val productToAdd = buildProduct()
        shoppingListMock.throwExceptionOnNextMethodCall()
        try {
            addProductUseCase.addProduct(productToAdd)
        } catch (e: Exception) {

        }
        assertShoppingListAndItsRepositoryHasTheSameData()
    }

    private fun buildProduct() = Product("description")
}