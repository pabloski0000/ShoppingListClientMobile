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
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.application.ServerShoppingListObserver
import main.shoppilientmobile.shoppingList.application.ServerShoppingListRemoteDataSource

class ServerShoppingListApi(
    private val streamingHttpClient: StreamingHttpClient,
    private val securityTokenKeeper: SecurityTokenKeeper,
) : ServerShoppingListRemoteDataSource {
    private val coroutineScopeOnMainThread = CoroutineScope(Dispatchers.Main)
    private val currentServerShoppingListState = mutableMapOf<String, String>()

    override fun observeServerShoppingList(observer: ServerShoppingListObserver) {
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
                            currentServerShoppingListState[productJson.getValue("id").jsonPrimitive.content] =
                                productJson.getValue("text").jsonPrimitive.content
                            notifyObserverOfCurrentListState(observer, currentServerShoppingListState)
                        }
                    }
                    "addedItem" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            val productDescription = productJson.getValue("text")
                                .jsonPrimitive
                                .content
                            currentServerShoppingListState[productId] = productDescription
                            notifyObserverOfProductAdded(observer, Product(productDescription))
                        }
                    }
                    "modifiedItem" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            val newProductDescription = productJson.getValue("text")
                                .jsonPrimitive
                                .content
                            val oldProductDescription = currentServerShoppingListState[productId]!!
                            currentServerShoppingListState[productId] = newProductDescription
                            notifyObserverOfProductModified(
                                observer,
                                Product(oldProductDescription),
                                Product(newProductDescription),
                            )
                        }
                    }
                    "deletedItem" -> {
                        withContext(Dispatchers.Main) {
                            val productJson = jsonResponse.getValue("item").jsonObject
                            val productId = productJson.getValue("id").jsonPrimitive.content
                            val productDescription = currentServerShoppingListState[productId]!!
                            currentServerShoppingListState.remove(productId)
                            notifyObserverOfProductDeleted(
                                observer,
                                Product(productDescription),
                            )
                        }
                    }
                }
            }.collect()
        }
    }

    private fun notifyObserverOfCurrentListState(observer: ServerShoppingListObserver, currentListState: Map<String, String>) {
        observer.stateAtTheMomentOfSubscribing(
            currentListState.values.map { productDescription ->
                Product(productDescription)
            }
        )
    }

    private fun notifyObserverOfProductAdded(observer: ServerShoppingListObserver, product: Product) {
        observer.productAdded(product)
    }

    private fun notifyObserverOfProductModified(
        observer: ServerShoppingListObserver,
        oldProduct: Product,
        newProduct: Product
    ) {
        observer.productModified(oldProduct, newProduct)
    }

    private fun notifyObserverOfProductDeleted(observer: ServerShoppingListObserver, product: Product) {
        observer.productDeleted(product)
    }
}