package main.shoppilientmobile.shoppingList.infrastructure.presentation

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.domainExposure.ProductBuilder
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.shoppingList.application.AddProductUseCase
import main.shoppilientmobile.shoppingList.application.DeleteProductUseCase
import main.shoppilientmobile.shoppingList.application.ModifyProductUseCase
import main.shoppilientmobile.shoppingList.application.Response
import main.shoppilientmobile.shoppingList.application.RequestExceptionListener
import main.shoppilientmobile.shoppingList.application.SharedShoppingListObserver
import main.shoppilientmobile.shoppingList.application.SynchroniseWithRemoteShoppingListUseCase
import main.shoppilientmobile.userRegistrationFeature.useCases.useCasesInputOutputs.GetLocalUserUseCase

class ShoppingListViewModelShared(
    private val synchroniseWithRemoteShoppingListUseCase: SynchroniseWithRemoteShoppingListUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val modifyProductUseCase: ModifyProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getLocalUserUseCase: GetLocalUserUseCase? = null,
) : KMMViewModel(), SharedShoppingListObserver {
    private val _productItemsUiState = MutableStateFlow(viewModelScope, emptyList<ProductItemState>())
    @NativeCoroutinesState
    val productItemsUiState = _productItemsUiState.asStateFlow()
    private val _productCouldNotBeAddedErrorMessage = MutableStateFlow("")
    @NativeCoroutinesState
    val productCouldNotBeAddedErrorMessage = _productCouldNotBeAddedErrorMessage.asStateFlow()
    private val _userIsAdmin = MutableStateFlow(false)
    val userIsAdmin = _userIsAdmin.asStateFlow()

    init {
        synchroniseWithRemoteShoppingListUseCase.synchronise(this)
        viewModelScope.coroutineScope.launch {
            _userIsAdmin.update { userIsAdmin() }
        }
    }

    fun loadSharedShoppingAndNotifyChanges() {

    }

    fun selectProductItem(index: Int) {
        _productItemsUiState.update {
            it.mapIndexed { indexInList, productItemState ->
                if (indexInList == index) {
                    return@mapIndexed productItemState.copy(selected = true)
                }
                return@mapIndexed productItemState
            }
        }
    }

    fun deselectProductItem(index: Int) {
        _productItemsUiState.update {
            it.mapIndexed { indexInList, productItemState ->
                if (indexInList == index) {
                    return@mapIndexed productItemState.copy(selected = false)
                }
                return@mapIndexed productItemState
            }
        }
    }

    fun selectAllProductsItems() {
        _productItemsUiState.update {
            it.map { productItem ->
                if (! productItem.selected) {
                    return@map productItem.copy(selected = true)
                }
                return@map productItem
            }
        }
    }

    fun deselectAllProductItems() {
        _productItemsUiState.update {
            it.map { productItem ->
                if (productItem.selected) {
                    return@map productItem.copy(selected = false)
                }
                return@map productItem
            }
        }
    }

    fun addProduct(product: String, response: Response) {
        addProductUseCase.addProduct(ProductBuilder().assignDescription(product).build()) { errorExplanation ->
            viewModelScope.coroutineScope.launch(Dispatchers.Main) {
                response(errorExplanation)
            }
        }
    }

    fun modifyProduct(oldProduct: String, newProduct: String, response: Response) {
        _productItemsUiState.value.forEach { product ->
            if (product.content == oldProduct) {
                modifyProductUseCase.modifyProduct(Product(oldProduct), Product(newProduct)) { errorExplanation ->
                    viewModelScope.coroutineScope.launch(Dispatchers.Main) {
                        response(errorExplanation)
                    }
                }
            }
        }
    }

    suspend fun deleteAllProducts() {
        _productItemsUiState.value.map { productItem: ProductItemState ->
            deleteProductUseCase.deleteProduct(productItem.toProduct())
        }
    }

    suspend fun deleteSelectedProducts() {
        val selectedProducts = _productItemsUiState.value.filter { it.selected }
        selectedProducts.map { productItem: ProductItemState ->
            deleteProductUseCase.deleteProduct(productItem.toProduct())
        }
    }

    suspend fun deleteProducts(products: List<String>) {
        _productItemsUiState.value.forEach { product ->
            if (product.content in products) {
                deleteProductUseCase.deleteProduct(product.toProduct())
            }
        }
    }

    override fun currentState(products: List<Product>) {
        _productItemsUiState.update {
            products.map { product ->
                ProductItemState(product.description, false)
            }
        }
    }

    override fun productAdded(product: Product) {
        _productItemsUiState.update {
            listOf(
                ProductItemState(product.description, false),
                *it.toTypedArray(),
            )
        }
    }

    override fun productModified(oldProduct: Product, newProduct: Product) {
        _productItemsUiState.update {
            it.map { product ->
                if (product.toProduct() == oldProduct) {
                    return@map ProductItemState(newProduct.description, false)
                }
                return@map product
            }
        }
    }

    override fun productDeleted(product: Product) {
        _productItemsUiState.update {
            it.filter { productItemState -> productItemState.toProduct() != product }
        }
    }

    private suspend fun userIsAdmin(): Boolean {
        val user = getLocalUserUseCase?.getLocalUser()
        return user is User && user.getRole() == UserRole.ADMIN
    }
}