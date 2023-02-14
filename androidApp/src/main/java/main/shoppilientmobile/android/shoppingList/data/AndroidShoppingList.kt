package main.shoppilientmobile.android.shoppingList.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main.shoppilientmobile.shoppingList.domain.ShoppingList
import main.shoppilientmobile.shoppingList.domain.ShoppingListObserver
import main.shoppilientmobile.domain.Product

class AndroidShoppingList(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingList {
    private val coroutineScopeInMainThread = CoroutineScope(Dispatchers.Main)
    private val products = MutableStateFlow(emptyList<Product>())
    private var observers = emptyList<ShoppingListObserver>()
    override fun recreate(products: List<Product>) {
        coroutineScopeInMainThread.launch(Dispatchers.IO) {
            shoppingListDao.repopulate(products)
            withContext(Dispatchers.Main) {
                notifyOfShoppingListRecreation(products)
            }
        }
    }

    override fun addProduct(product: Product) {
        coroutineScopeInMainThread.launch(Dispatchers.IO) {
            shoppingListDao.insertProduct(product)
            withContext(Dispatchers.Main) {
                notifyOfProductAdded(product)
            }
        }
    }

    override fun getProducts(): List<Product> {
        return emptyList()
    }

    override fun modifyProduct(oldProduct: Product, newProduct: Product) {
        coroutineScopeInMainThread.launch(Dispatchers.IO) {
            shoppingListDao.updateProductDescription(oldProduct, newProduct)
            withContext(Dispatchers.Main) {
                notifyOfProductModified(
                    oldProduct = oldProduct,
                    newProduct = newProduct,
                )
            }
        }
    }

    override fun deleteProducts(products: List<Product>) {
        coroutineScopeInMainThread.launch(Dispatchers.IO) {
            shoppingListDao.deleteProducts(products)
            withContext(Dispatchers.Main) {
                products.map { product ->
                    notifyOfProductDeleted(product)
                }
            }
        }
    }

    override fun deleteProduct(product: Product) {
        coroutineScopeInMainThread.launch(Dispatchers.IO) {
            shoppingListDao.deleteProducts(listOf(product))
            withContext(Dispatchers.Main) {
                notifyOfProductDeleted(product)
            }
        }
    }

    override fun observe(): Flow<List<Product>> {
        return products
    }

    override fun observeShoppingList(observer: ShoppingListObserver) {
        coroutineScopeInMainThread.launch(Dispatchers.IO) {
            val currentStateOfList = shoppingListDao.getProducts()
            notifyObserverOfCurrentListState(observer, currentStateOfList)
        }
        observers = listOf(*observers.toTypedArray(), observer)
    }

    private fun notifyObserverOfCurrentListState(
        observer: ShoppingListObserver,
        currentList: List<Product>
    ) {
        observer.stateAtTheMomentOfSubscribing(currentList)
    }

    private fun notifyOfProductAdded(product: Product) {
        observers.map { it.productAdded(product) }
    }

    private fun notifyOfProductModified(oldProduct: Product, newProduct: Product) {
        observers.map {
            it.productModified(
                oldProduct = oldProduct,
                newProduct = newProduct,
            )
        }
    }

    private fun notifyOfProductDeleted(product: Product) {
        observers.map { it.productDeleted(product) }
    }

    private fun notifyOfShoppingListRecreation(products: List<Product>) {
        observers.map { it.shoppingListRecreated(products) }
    }
}