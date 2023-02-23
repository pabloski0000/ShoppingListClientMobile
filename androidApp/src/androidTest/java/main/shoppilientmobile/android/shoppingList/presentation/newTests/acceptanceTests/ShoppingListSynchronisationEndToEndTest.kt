package main.shoppilientmobile.android.shoppingList.presentation.newTests.acceptanceTests

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.App
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.shoppingList.presentation.AndroidShoppingListUI
import main.shoppilientmobile.android.shoppingList.presentation.Product
import main.shoppilientmobile.android.shoppingList.presentation.testDoubles.ExternalShoppingListSpy
import main.shoppilientmobile.android.shoppingList.presentation.testDoubles.LocalShoppingListSpy
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListSynchronisationEndToEndTest {
    private lateinit var shoppingListUI: AndroidShoppingListUI
    private lateinit var externalShoppingList: RemoteShoppingList
    private lateinit var localShoppingListSpy: LocalShoppingListSpy
    private lateinit var externalShoppingListSpy: ExternalShoppingListSpy
    private companion object {
        var settingUpTestForTheFirstTime = true
        lateinit var container: AndroidContainer
    }

    @Before
    fun setUp() {
        if (settingUpTestForTheFirstTime) {
            val app = App(ApplicationProvider.getApplicationContext())
            container = app.run()
            app.registerUserOnServerBlokingly("pablos", UserRole.ADMIN)
            settingUpTestForTheFirstTime = false
        }
        externalShoppingList = container.remoteShoppingList
        externalShoppingList.deleteAllProducts()
        shoppingListUI = container.androidShoppingListUI
        localShoppingListSpy = LocalShoppingListSpy()
        externalShoppingListSpy = ExternalShoppingListSpy()
        shoppingListUI.observeShoppingList(localShoppingListSpy)
        externalShoppingList.observe(externalShoppingListSpy)
    }

    @Test
    fun addProducts() {
        val products = listOf(
            Product("Banana"),
            Product("Apple"),
            Product("Lemon"),
        )
        shoppingListUI.addProducts(products)
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            products.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(products)
    }

    @Test
    fun modifyProducts() {
        val oldProducts = listOf(
            Product("Banana"),
            Product("Apple"),
            Product("Lemon"),
        )
        val modifiedProductIndex = 1
        val modifiedProducts = oldProducts.mapIndexed { index, product ->
            if (index == modifiedProductIndex) {
                return@mapIndexed Product("Strawberry")
            }
            return@mapIndexed product
        }
        shoppingListUI.addProducts(oldProducts)
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            oldProducts.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(oldProducts)
        shoppingListUI.modifyProduct(
            oldProducts[modifiedProductIndex],
            modifiedProducts[modifiedProductIndex],
        )
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            modifiedProducts.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            modifiedProducts
        )
    }

    fun removeProduct() {
        /*val products = listOf(
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
        fakeShoppingListUI.assertProductsExistOrThrowException(remainingProducts)*/
    }

    private fun waitUntilRemoteShoppingListResponseHasBeenProcessed() {
        runBlocking { delay(500) }
    }
}