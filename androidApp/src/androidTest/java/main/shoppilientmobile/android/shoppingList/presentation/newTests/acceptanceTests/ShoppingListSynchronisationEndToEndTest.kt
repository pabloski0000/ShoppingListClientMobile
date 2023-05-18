package main.shoppilientmobile.android.shoppingList.presentation.newTests.acceptanceTests

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.App
import main.shoppilientmobile.android.core.AndroidContainer
import main.shoppilientmobile.android.shoppingList.presentation.AndroidShoppingListUI
import main.shoppilientmobile.android.shoppingList.presentation.Product
import main.shoppilientmobile.android.shoppingList.presentation.testDoubles.ExternalShoppingListSpy
import main.shoppilientmobile.android.shoppingList.presentation.testDoubles.LocalShoppingListSpy
import main.shoppilientmobile.shoppingList.application.RemoteShoppingList
import main.shoppilientmobile.shoppingList.application.RequestExceptionListener
import main.shoppilientmobile.userRegistrationFeature.useCases.RegisterAdminUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.GetLocalUserUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListSynchronisationEndToEndTest {
    private lateinit var shoppingListUI: AndroidShoppingListUI
    private lateinit var externalShoppingList: RemoteShoppingList
    private lateinit var getLocalUserUseCase: GetLocalUserUseCase
    private lateinit var registerAdminUseCase: RegisterAdminUseCase
    private companion object {
        var settingUpTestForTheFirstTime = true
        lateinit var container: AndroidContainer
        lateinit var localShoppingListSpy: LocalShoppingListSpy
        lateinit var externalShoppingListSpy: ExternalShoppingListSpy
    }

    @Before
    fun setUp() {
        runBlocking {
            if (settingUpTestForTheFirstTime) {
                val app = App(ApplicationProvider.getApplicationContext())
                container = app.run()
                getLocalUserUseCase = container.getLocalUserUseCase
                registerAdminUseCase = container.registrationContainer!!.registerAdminUseCase
                runBlocking {
                    if (getLocalUserUseCase.getLocalUser() == null) {
                        registerAdminUseCase.registerAdmin("pabloski0000")
                    }
                }
                localShoppingListSpy = LocalShoppingListSpy()
                externalShoppingListSpy = ExternalShoppingListSpy()
                settingUpTestForTheFirstTime = false
            }
            externalShoppingList = container.remoteShoppingList
            externalShoppingList.deleteAllProducts()
            shoppingListUI = container.androidShoppingListUI
            shoppingListUI.observeShoppingList(localShoppingListSpy)
            externalShoppingList.subscribe(externalShoppingListSpy)
        }
    }

    @Test
    fun addProducts() {
        val products = listOf(
            Product("Banana"),
            Product("Apple"),
            Product("Lemon"),
        )
        runBlocking {
            shoppingListUI.addProducts(products, object : RequestExceptionListener {
                override fun informUserOfError(explanation: String) {
                    //TODO("Not yet implemented")
                }

            })
        }
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
        runBlocking {
            shoppingListUI.addProducts(oldProducts, object : RequestExceptionListener {
                override fun informUserOfError(explanation: String) {
                    //TODO("Not yet implemented")
                }

            })
        }
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            oldProducts.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(oldProducts)
        runBlocking {
            shoppingListUI.modifyProduct(
                oldProducts[modifiedProductIndex],
                modifiedProducts[modifiedProductIndex],
            )
        }
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
        runBlocking {
            shoppingListUI.addProducts(shoppingListStateBeforeDeletion, object : RequestExceptionListener {
                override fun informUserOfError(explanation: String) {
                    //TODO("Not yet implemented")
                }

            })
        }
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateBeforeDeletion.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateBeforeDeletion
        )
        runBlocking {
            shoppingListUI.deleteProduct(
                shoppingListStateBeforeDeletion[productToDeleteIndex]
            )
        }
        externalShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateAfterDeletion.map { it.toProduct() }
        )
        localShoppingListSpy.assertShoppingListStateIsExactlyThisOrThrowException(
            shoppingListStateAfterDeletion
        )
    }
}