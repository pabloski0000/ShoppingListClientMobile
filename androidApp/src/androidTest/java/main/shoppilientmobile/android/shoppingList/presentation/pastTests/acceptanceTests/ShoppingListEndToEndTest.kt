package main.shoppilientmobile.android.shoppingList.presentation.pastTests.acceptanceTests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import main.shoppilientmobile.android.MainActivity
import main.shoppilientmobile.android.R
import main.shoppilientmobile.android.shoppingList.presentation.testUtils.ProductItemCreator.createRandomProduct
import main.shoppilientmobile.android.shoppingList.presentation.testUtils.ProductItemCreator.createRandomProducts
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListEndToEndTest {
    @get:Rule
    lateinit var composableRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
    private val coroutineOnThreadDifferentToMain = CoroutineScope(Dispatchers.Default)

    init {
        startApplicationAndGoToShoppingListScreen()
    }

    @Before
    fun setUp() {
        clearList()
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
            addProductToGUI(product)
            checkIfProductIsOnTheScreen(product)
        }
    }

    @Test
    fun removeProducts() {
        val amountOfProductsToCreate = 7
        val products = createRandomProducts(amountOfProductsToCreate)
        addProductsToGUI(products)
        val productsToRemove = products.subList(0, 4)
        removeProductsOnTheScreen(productsToRemove)
        checkThatProductsDoNotExistOnTheScreen(productsToRemove)
    }

    @Test
    fun modifyProducts() {
        val products = createRandomProducts(amountOfProducts = 4)
        val oldAndNewProducts = mapOf(
            products[0] to createRandomProduct(),
            products[1] to createRandomProduct(),
            products[2] to createRandomProduct(),
            products[3] to createRandomProduct(),
        )
        addProductsToGUI(products)
        modifyProductsThatAreOnTheScreen(oldAndNewProducts)
        checkThatProductToModifyHasChanged(oldAndNewProducts)
    }

    @Test
    fun checkThatApplicationPersistTheList() {
        val products = listOf("Banana", "Apple", "Coffee")
        addDataBeforeClosingApp(products)
        restartApp()
        checkThatDataStillExists(products)
    }

    private fun modifyProduct(product: String): String {
        return "$product kjlasd"
    }
    private fun clearList() {
        removeAllProducts()
    }

    private fun removeAllProducts() {
        val activity = composableRule.activity
        val productIdentifier = activity.getString(R.string.product)
        if (isThereAnyProduct()) {
            composableRule.onAllNodesWithTag(
                productIdentifier
            ).onFirst().performTouchInput { longClick() }
            composableRule.onNodeWithTag(
                activity.getString(R.string.select_all)
            ).performClick()
            composableRule.onNode(getDeletionIcon()).performClick()
        }
    }

    private fun isThereAnyProduct(): Boolean {
        val productIdentifier = composableRule.activity.getString(R.string.product)
        return try {
            composableRule.onAllNodesWithTag(
                productIdentifier
            ).assertAny(
                hasTestTag(productIdentifier)
            )
            true
        } catch (e: AssertionError) {
            false
        }
    }

    private fun addDataBeforeClosingApp(products: List<String>) {
        addProductsToGUI(products)
    }

    private fun restartApp() {
        composableRule.activity.finish()
        composableRule.activityRule.scenario.onActivity {
            coroutineOnThreadDifferentToMain.launch {
                launchActivity<MainActivity>()
            }
        }
        //composableRule.activity.startActivity(Intent(composableRule.activity.applicationContext, MainActivity::class.java))
    }

    private fun checkThatDataStillExists(products: List<String>) {
        checkIfProductsAreOnTheScreen(products)
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

    private fun addProductsToGUI(
        products: List<String>,
    ) {
        products.map {
            addProductToGUI(it)
        }
    }

    private fun addProductToGUI(
        product: String,
    ) {
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

    private fun checkIfProductsAreOnTheScreen(products: List<String>) {
        products.map { product ->
            checkIfProductIsOnTheScreen(product)
        }
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