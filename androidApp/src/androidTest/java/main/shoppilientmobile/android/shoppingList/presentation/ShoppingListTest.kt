package main.shoppilientmobile.android.shoppingList.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.android.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListTest {
    @get:Rule
    lateinit var composableRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

    init {
        startApplicationAndGoToShoppingListScreen()
    }

    private fun startApplicationAndGoToShoppingListScreen() {
        composableRule = createAndroidComposeRule(MainActivity::class.java)
    }

    @Test
    fun addProducts() {
        val amountOfProductsToCreate = 20
        val products = createRandomProducts(amountOfProductsToCreate)
        for (i in 0 until amountOfProductsToCreate) {
            val product = products[i]
            addProduct(product)
            checkIfProductIsOnTheScreen(product)
        }
    }

    @Test
    fun removeProducts() {
        val amountOfProductsToCreate = 6
        val products = createRandomProducts(amountOfProductsToCreate)
        addProducts(products)
        val productsToRemove = products.subList(0, 5)
        removeProductsOnTheScreen(productsToRemove)
        checkThatProductsDoNotExistOnTheScreen(productsToRemove)
    }

    @Test
    fun modifyProduct() {
        val originalProduct = "orange"
        val modifiedProduct = "Apple"
        addProduct(originalProduct)
        composableRule.onNode(getProduct(originalProduct)).performClick()
        composableRule.onNode(getProductInputText()).performTextInput(modifiedProduct)
        composableRule.onNode(getProductInputText()).performImeAction()
        sleepThreadAsMuchTimeAsAUserWouldExpectOperationsToLastUntilFinished()
        composableRule.onNode(getProduct(originalProduct)).assertDoesNotExist()
        composableRule.onNode(getProduct(modifiedProduct)).assertExists()
    }

    @Test
    fun modifyProducts() {
        val products = createRandomProducts(amountOfProducts = 4)
        val oldAndNewProducts = mapOf(
            products[0] to createRandomProduct(),
            products[1] to createRandomProduct(),
            products[3] to createRandomProduct(),
        )
        addProducts(products)
        modifyProductsThatAreOnTheScreen(oldAndNewProducts)
        checkThatProductToModifyHasChanged(oldAndNewProducts)
    }

    private fun checkThatProductToModifyHasChanged(oldAndNewProducts: Map<String, String>) {
        checkThatOldProductsDoNotExist(oldAndNewProducts.keys.toList())
        checkThatNewProductsExist(oldAndNewProducts.values.toList())
    }

    private fun checkThatOldProductsDoNotExist(products: List<String>) {
        products.map { product ->
            composableRule.onNode(getProduct(product)).assertDoesNotExist()
        }
    }

    private fun checkThatNewProductsExist(products: List<String>) {
        products.map { product ->
            composableRule.onNode(getProduct(product)).assertExists()
        }
    }

    private fun modifyProductsThatAreOnTheScreen(oldAndNewProducts: Map<String, String>) {
        oldAndNewProducts.map { entry ->
            clickOnProduct(entry.key)
            rewriteProduct(entry.value)
            sayItIsDone()
        }
    }

    private fun rewriteProduct(product: String) {
        composableRule.onNode(getProductInputText()).performTextInput(product)
    }

    private fun sayItIsDone() {
        composableRule.onNode(getProductInputText()).performImeAction()
    }

    private fun createRandomProducts(amountOfProducts: Int): List<String> {
        val products = mutableListOf<String>()
        for (i in 1..amountOfProducts) {
            products.add(
                createRandomProduct()
            )
        }
        return products
    }

    private fun createRandomProduct(): String {
        val productLength = 5..11
        val allowedCharacters = ('a'..'z') + ('A'..'Z')
        val product = buildString() {
            for (i in 0 until productLength.random()) {
                append(allowedCharacters.random().toString())
            }
        }
        return product
    }

    private fun removeProductsOnTheScreen(products: List<String>) {
        val firstProduct = products[0]
        longClickOnProduct(firstProduct)
        clickOnProducts(products.filter { it != firstProduct })
        clickOnDelete()
    }

    private fun longClickOnProduct(product: String) {
        composableRule.onNode(
            getProduct(product)
        ).performTouchInput { longClick() }
    }

    private fun clickOnProducts(products: List<String>) {
        products.map { product ->
            clickOnProduct(product)
        }
    }

    private fun clickOnProduct(product: String) {
        composableRule.onNode(
            getProduct(product)
        ).performClick()
    }

    private fun clickOnDelete() {
        composableRule.onNode(getDeletionIcon()).performClick()
    }

    private fun addProducts(products: List<String>) {
        products.map {
            addProduct(it)
        }
    }

    private fun addProduct(product: String) {
        composableRule.onNode(
            hasTestTag("AddProduct")
        ).performClick()
        val productFactory = getProductInputText()
        composableRule.onNode(
            productFactory
        ).performTextInput(product)
        composableRule.onNode(
            productFactory
        ).performImeAction()
    }

    private fun checkIfProductIsOnTheScreen(product: String) {
        composableRule.onNode(
            hasTestTag("Product") and hasText(product)
        ).assertExists()
    }

    private fun checkThatProductsDoNotExistOnTheScreen(products: List<String>) {
        products.map {product ->
            composableRule.onNode(getProduct(product)).assertDoesNotExist()
        }
    }

    private fun getProducts(products: List<String>): List<SemanticsMatcher> {
        val productsUi = mutableListOf<SemanticsMatcher>()
        for (product in products) {
            productsUi.add(getProduct(product))
        }
        return productsUi
    }

    private fun getProduct(product: String): SemanticsMatcher {
        return hasTestTag("Product") and hasText(product)
    }

    private fun getDeletionIcon(): SemanticsMatcher {
        return hasTestTag("DeletionIcon")
    }

    private fun getProductInputText(): SemanticsMatcher {
        return hasTestTag("ProductInputText")
    }

    private fun sleepThreadAsMuchTimeAsAUserWouldExpectOperationsToLastUntilFinished() {
        runBlocking { sleep(10) }
    }

    private suspend fun sleep(milliseconds: Long) {
        delay(milliseconds)
    }
}