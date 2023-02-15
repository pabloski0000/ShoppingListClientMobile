package main.shoppilientmobile.shoppingList.newTests

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.domain.domainExposure.ProductBuilder
import main.shoppilientmobile.shoppingList.application.ShoppingListUiListener
import main.shoppilientmobile.shoppingList.testDoubles.FakeRemoteShoppingList
import main.shoppilientmobile.shoppingList.testDoubles.FakeShoppingListUI
import kotlin.test.BeforeTest
import kotlin.test.Test

class ShoppingListSynchronisationEndToEndTest {
    private val fakeShoppingListUI = FakeShoppingListUI()
    private val fakeRemoteShoppingList = FakeRemoteShoppingList()
    private lateinit var shoppingListUiListener: ShoppingListUiListener

    @BeforeTest
    fun setUp() {
        shoppingListUiListener = ShoppingListUiListener(
            fakeShoppingListUI,
            fakeRemoteShoppingList,
        )
        fakeShoppingListUI.observerShoppingList()
    }

    @Test
    fun addProduct() {
        val products = listOf(
            ProductBuilder().assignDescription("Banana").build(),
            ProductBuilder().assignDescription("Apple").build(),
            ProductBuilder().assignDescription("Lemon").build(),
        )
        fakeShoppingListUI.addProducts(products)
        waitUntilRemoteShoppingListResponseHasBeenProcessed()
        fakeRemoteShoppingList.assertProductsExistOrThrowException(products)
        fakeShoppingListUI.assertProductsExistOrThrowException(products)
    }

    @Test
    fun modifyProduct() {
        val oldProducts = listOf(
            ProductBuilder().assignDescription("Banana").build(),
            ProductBuilder().assignDescription("Apple").build(),
            ProductBuilder().assignDescription("Lemon").build(),
        )
        val modifiedProductIndex = 1
        val modifiedProducts = oldProducts.mapIndexed { index, product ->
            if (index == modifiedProductIndex) {
                return@mapIndexed ProductBuilder().assignDescription("Strawberry").build()
            }
            return@mapIndexed product
        }
        fakeShoppingListUI.addProducts(oldProducts)
        waitUntilRemoteShoppingListResponseHasBeenProcessed()
        fakeRemoteShoppingList.assertProductsExistOrThrowException(oldProducts)
        fakeShoppingListUI.assertProductsExistOrThrowException(oldProducts)
        fakeShoppingListUI.modifyProduct(
            oldProducts[modifiedProductIndex],
            modifiedProducts[modifiedProductIndex]
        )
        waitUntilRemoteShoppingListResponseHasBeenProcessed()
        fakeRemoteShoppingList.assertProductsDoNotExistOrThrowException(
            listOf(oldProducts[modifiedProductIndex])
        )
        fakeShoppingListUI.assertProductsDoNotExistOrThrowException(
            listOf(oldProducts[modifiedProductIndex])
        )
        fakeRemoteShoppingList.assertProductsExistOrThrowException(modifiedProducts)
        fakeShoppingListUI.assertProductsExistOrThrowException(modifiedProducts)
    }

    @Test
    fun removeProduct() {
        val products = listOf(
            ProductBuilder().assignDescription("Banana").build(),
            ProductBuilder().assignDescription("Apple").build(),
            ProductBuilder().assignDescription("Lemon").build(),
        )
        val remainingProducts = products.subList(0, 1) + products.subList(2, products.size)
        fakeShoppingListUI.addProducts(products)
        waitUntilRemoteShoppingListResponseHasBeenProcessed()
        fakeRemoteShoppingList.assertProductsExistOrThrowException(products)
        fakeShoppingListUI.assertProductsExistOrThrowException(products)
        fakeShoppingListUI.removeProduct(products[1])
        waitUntilRemoteShoppingListResponseHasBeenProcessed()
        fakeRemoteShoppingList.assertProductsDoNotExistOrThrowException(listOf(products[1]))
        fakeShoppingListUI.assertProductsDoNotExistOrThrowException(listOf(products[1]))
        fakeRemoteShoppingList.assertProductsExistOrThrowException(remainingProducts)
        fakeShoppingListUI.assertProductsExistOrThrowException(remainingProducts)
    }

    private fun waitUntilRemoteShoppingListResponseHasBeenProcessed() {
        runBlocking { delay(100) }
    }
}