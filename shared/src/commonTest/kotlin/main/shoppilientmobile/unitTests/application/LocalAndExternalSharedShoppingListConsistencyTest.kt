package main.shoppilientmobile.unitTests.application

import main.shoppilientmobile.application.AddProductUseCase
import main.shoppilientmobile.application.ExternalSharedShoppingListSynchronizer
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.unitTests.application.mocks.ShoppingListMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalAndExternalSharedShoppingListConsistencyTest {
    private lateinit var localShoppingListMock: ShoppingListMock
    private lateinit var externalShoppingListMock: ShoppingListMock
    private lateinit var localProductAddition: AddProductUseCase
    private lateinit var externalSharedShoppingListSynchronizer: ExternalSharedShoppingListSynchronizer

    @BeforeTest
    fun setUp(){
        localShoppingListMock = ShoppingListMock()
        externalShoppingListMock = ShoppingListMock()
        localProductAddition = AddProductUseCase(localShoppingListMock)
        externalSharedShoppingListSynchronizer = ExternalSharedShoppingListSynchronizer(
            externalShoppingListMock,

        )
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

    private fun buildProduct() = Product("description")
}