package main.shoppilientmobile.unitTests.domain

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.ShoppingListImpl
import main.shoppilientmobile.domain.User
import kotlin.test.*

class ShoppingListImplTest {
    private lateinit var shoppingList: ShoppingListImpl

    @BeforeTest
    fun setUp(){
        shoppingList = ShoppingListImpl()
    }

    @Test
    fun assertItRegistersUserCorrectly(){
        //shoppingList.registerUser(User("pabloski"))
    }

    @Test
    fun assertItReturnsEveryProductThatWasAdded(){
        val productsAdded = mutableListOf<Product>()
        for (i in 0..3){
            productsAdded.add(buildProduct("$i"))
            shoppingList.addProduct(productsAdded[i])
        }
        assertEquals(productsAdded, shoppingList.getProducts())
    }

    @Test
    fun assertItAddsProductCorrectly(){
        val product = buildProduct()
        shoppingList.addProduct(product)
        assertEquals(product, shoppingList.getProducts().first())
    }

    @Test
    fun assertItDoesNotAddProductIfItContainsAnotherThatIsEquals() {
        val product = buildProduct()
            shoppingList.addProduct(product)
        try {
            shoppingList.addProduct(product)
        } catch (e: Exception) {

        }
        assertTrue {
            shoppingList.getProducts().size == 1
        }
    }

    @Test
    fun assertItModifiesAProductCorrectly(){
        val oldProduct = buildProduct("oldProduct")
        val newProduct = buildProduct("newProduct")
        shoppingList.addProduct(oldProduct)
        shoppingList.modifyProduct(oldProduct, newProduct)
        assertEquals(newProduct, shoppingList.getProducts().first())
    }

    @Test
    fun assertItRemovesAProductCorrectly(){
        val product = buildProduct()
        shoppingList.addProduct(product)
        shoppingList.removeProduct(product)
        assertEquals(emptyList(), shoppingList.getProducts())
    }

    @Test
    fun assertItInformsClientsIfItContainsAProductCorrectly(){
        val product = buildProduct()
        assertFalse {
            shoppingList.contains(product)
        }
        shoppingList.addProduct(product)
        assertTrue {
            shoppingList.contains(product)
        }
    }

    private fun buildProduct(description: String = "any") =  Product(description)
}