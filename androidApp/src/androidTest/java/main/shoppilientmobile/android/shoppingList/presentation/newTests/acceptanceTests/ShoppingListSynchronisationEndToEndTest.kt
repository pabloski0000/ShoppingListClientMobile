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
    private companion object {
        var settingUpTestForTheFirstTime = true
        lateinit var container: AndroidContainer
        lateinit var localShoppingListSpy: LocalShoppingListSpy
        lateinit var externalShoppingListSpy: ExternalShoppingListSpy
    }

    @Before
    fun setUp() {
        if (settingUpTestForTheFirstTime) {
            val app = App(ApplicationProvider.getApplicationContext())
            container = app.run()
            app.registerUserOnServerBlokingly("pablos", UserRole.ADMIN)
            localShoppingListSpy = LocalShoppingListSpy()
            externalShoppingListSpy = ExternalShoppingListSpy()
            settingUpTestForTheFirstTime = false
        }
        externalShoppingList = container.remoteShoppingList
        externalShoppingList.deleteAllProducts()
        shoppingListUI = container.androidShoppingListUI
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
            Product("Coconut"),
            Product("Skittles"),
            Product("Sweet"),
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

    @Test
    fun removeProduct() {
        val shoppingListStateBeforeDeletion = listOf(
            Product("Banana for remove"),
            Product("Apple for remove"),
            Product("Lemon for remove"),
        )
        val productToDeleteIndex = 1
        val shoppingListStateAfterDeletion = listOf(
            Product("Banana for remove"),
            Product("Lemon for remove"),
        )
        shoppingListUI.addProducts(shoppingListStateBeforeDeletion)
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateBeforeDeletion.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateBeforeDeletion
        )
        shoppingListUI.deleteProduct(
            shoppingListStateBeforeDeletion[productToDeleteIndex]
        )
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateAfterDeletion.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateAfterDeletion
        )
    }
}