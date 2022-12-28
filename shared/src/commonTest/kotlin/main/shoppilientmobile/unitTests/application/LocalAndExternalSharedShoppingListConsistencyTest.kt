package main.shoppilientmobile.unitTests.application

import main.shoppilientmobile.application.ExternalSharedShoppingListSynchronizer
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.application.applicationExposure.ExternalSharedShoppingList
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.unitTests.application.mocks.ExternalSharedShoppingListMock
import main.shoppilientmobile.unitTests.application.mocks.ObservableSharedShoppingListMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExternalSharedShoppingListSynchronizationTest {
    private lateinit var localShoppingListMock: ObservableSharedShoppingListMock
    private lateinit var externalShoppingListMock: ExternalSharedShoppingList
    private lateinit var externalSharedShoppingListSynchronizer: ExternalSharedShoppingListSynchronizer

    @BeforeTest
    fun setUp(){
        localShoppingListMock = ObservableSharedShoppingListMock()
        externalShoppingListMock = ExternalSharedShoppingListMock()
        externalSharedShoppingListSynchronizer = ExternalSharedShoppingListSynchronizer(
            localShoppingListMock,
            externalShoppingListMock,
        )
    }

    @Test
    fun assertItSynchronizesExternalSharedShoppingListCorrectly(){
        val product = buildProduct(productDescription = "banana")
        addProduct(product)
        assertExternalSharedShoppingListHasSameContent()
        val modifiedProduct = product.copy(description = "apple")
        modifyProduct(product, modifiedProduct)
        assertExternalSharedShoppingListHasSameContent()
        removeProduct(modifiedProduct)
        assertExternalSharedShoppingListHasSameContent()
        registerUser(UserBuilderImpl().build())
        assertExternalSharedShoppingListHasSameContent()
    }

    private fun addProduct(product: Product) {
        localShoppingListMock.addProduct(product)
    }

    private fun modifyProduct(existingProduct: Product, modifiedProduct: Product) {
        localShoppingListMock.modifyProduct(existingProduct, modifiedProduct)
    }

    private fun removeProduct(product: Product) {
        localShoppingListMock.addProduct(product)
    }

    private fun registerUser(user: User) {
        localShoppingListMock.registerUser(user)
    }

    private fun assertExternalSharedShoppingListHasSameContent() {
        assertEquals(localShoppingListMock.getProducts(), externalShoppingListMock.getProducts())
    }

    private fun buildProduct(productDescription: String = "default") = Product(productDescription)
}