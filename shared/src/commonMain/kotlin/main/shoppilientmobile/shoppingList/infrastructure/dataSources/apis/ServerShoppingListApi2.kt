package main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.core.remote.HttpMethod
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.core.remote.StreamingHttpClient
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.shoppingList.infrastructure.ServerShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ProductOnServerShoppingList

class ServerShoppingListApi2(
    private val streamingHttpClient: StreamingHttpClient,
    private val securityTokenKeeper: SecurityTokenKeeper,
) {
    private val coroutineScopeOnMainThread = CoroutineScope(Dispatchers.Main)

    fun observeServerShoppingList(observer: ServerShoppingListObserver) {
        coroutineScopeOnMainThread.launch(Dispatchers.Default) {
            val httpRequest = HttpRequest(
                httpMethod = HttpMethod.GET,
                url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products/synchronise-with-shopping-list",
                headers = mapOf(
                    "Accept" to "application/x-ndjson",
                    "Authorization" to "Bearer ${securityTokenKeeper.getSecurityToken()}",
                ),
                body = "",
            )
            val response = streamingHttpClient.makeRequest(httpRequest)
            response.map { responseChunk ->
                val jsonResponse = Json.parseToJsonElement(responseChunk).jsonObject
                val typeOfNotification = jsonResponse.getValue("typeOfNotification")
                when (typeOfNotification.jsonPrimitive.content) {
                    "currentState" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            val productDescription = productJson.getValue("text")
                                .jsonPrimitive
                                .content
                            val product = createServerShoppingListProduct(productId, productDescription)
                            notifyObserverOfCurrentListState(
                                observer,
                                product,
                            )
                        }
                    }
                    "addedItem" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            val productDescription = productJson.getValue("text")
                                .jsonPrimitive
                                .content
                            val addedProduct = createServerShoppingListProduct(
                                productId,
                                productDescription,
                            )
                            notifyObserverOfProductAdded(observer, addedProduct)
                        }
                    }
                    "modifiedItem" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            val modifiedProductDescription = productJson.getValue("text")
                                .jsonPrimitive
                                .content
                            val modifiedProduct = createServerShoppingListProduct(
                                productId,
                                modifiedProductDescription,
                            )
                            notifyObserverOfProductModified(
                                observer,
                                modifiedProduct,
                            )
                        }
                    }
                    "deletedItem" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            notifyObserverOfProductDeleted(
                                observer,
                                productId,
                            )
                        }
                    }
                }
            }.collect()
        }
    }

    private fun notifyObserverOfCurrentListState(
        observer: ServerShoppingListObserver,
        product: ProductOnServerShoppingList,
    ) {
        observer.currentState(product)
    }

    private fun notifyObserverOfProductAdded(
        observer: ServerShoppingListObserver,
        product: ProductOnServerShoppingList,
    ) {
        observer.productAdded(product)
    }

    private fun notifyObserverOfProductModified(
        observer: ServerShoppingListObserver,
        modifiedProduct: ProductOnServerShoppingList,
    ) {
        observer.productModified(modifiedProduct)
    }

    private fun notifyObserverOfProductDeleted(
        observer: ServerShoppingListObserver,
        productId: String,
    ) {
        observer.productDeleted(productId)
    }

    private fun createServerShoppingListProduct(id: String, description: String): ProductOnServerShoppingList {
        return ProductOnServerShoppingList(id, description)
    }
}