package main.shoppilientmobile.shoppingList.application

import main.shoppilientmobile.domain.exceptions.ThereCannotBeTwoProductsWithTheSameNameException
import main.shoppilientmobile.shoppingList.application.doubles.AddProductUseCase2UiSpy
import main.shoppilientmobile.shoppingList.application.doubles.ProductRepositoryFake
import kotlin.test.*

class AddProductUseCase2Test {
    private val productRepositoryFake = ProductRepositoryFake()
    private val logSystemSpy = LogSystemSpy()
    private val addProductUseCase2UiSpy = AddProductUseCase2UiSpy()
    private val addProductUseCase2 = AddProductUseCase2(productRepositoryFake, addProductUseCase2UiSpy, logSystemSpy)

    @BeforeTest
    fun setUp() {
        logSystemSpy.clear()
    }

    @Test
    fun addProduct() {
        assertEquals(expected = 0, logSystemSpy.infoMessages.size)
        val banana = "Banana"
        addProductUseCase2.add(banana)
        assertEquals(
            expected = 1,
            productRepositoryFake.products.filter { product ->
                product.description == banana
            }.size,
        )
        assertEquals(expected = 1, logSystemSpy.infoMessages.size)
    }

    @Test
    fun addProductThatAlreadyExists() {
        val apple = "Apple"
        val appleRepeated = apple
        addProductUseCase2.add(apple)
        assertTrue { addProductUseCase2UiSpy.hasInformedUserOfDuplicateProduct == false }
        assertTrue { logSystemSpy.warningMessages.size == 0 }
        addProductUseCase2.add(appleRepeated)
        assertTrue { addProductUseCase2UiSpy.hasInformedUserOfDuplicateProduct == true }
        assertTrue { logSystemSpy.warningMessages.size == 1 }
    }

    @Test
    fun addProductWithAShorterLengthThanTheMandatoryOne() {

    }
}