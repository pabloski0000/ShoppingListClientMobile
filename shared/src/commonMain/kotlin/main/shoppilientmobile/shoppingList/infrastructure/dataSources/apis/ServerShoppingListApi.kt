package main.shoppilientmobile.shoppingList.infrastructure.dataSources.apis

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.*
import main.shoppilientmobile.core.remote.HttpMethod
import main.shoppilientmobile.core.remote.HttpRequest
import main.shoppilientmobile.core.remote.HttpRequestBuilder
import main.shoppilientmobile.core.remote.NonBlockingHttpClient
import main.shoppilientmobile.core.remote.StreamingHttpClient
import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.domain.exceptions.ThereCannotBeTwoProductsWithTheSameNameException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionExceedsMaximumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDescriptionIsShorterThanMinimumLengthException
import main.shoppilientmobile.domain.exceptions.ProductDoesNotExistException
import main.shoppilientmobile.shoppingList.infrastructure.ServerShoppingListObserver
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ProductOnServerShoppingList
import kotlin.coroutines.cancellation.CancellationException

class ServerShoppingListApi(
    private val streamingHttpClient: StreamingHttpClient,
    private val nonBlockingHttpClient: NonBlockingHttpClient,
) {
    private val coroutineScopeOnMainThread = CoroutineScope(Dispatchers.Main)
    private var sharedShoppingListObserver: ServerShoppingListObserver? = null
    private var listenToListBackgroundProcess: Job? = null
    private val listeningToList
        get() = listenToListBackgroundProcess is Job

    fun subscribeToSharedShoppingList(observer: ServerShoppingListObserver) {
        sharedShoppingListObserver = observer
    }

    fun listenToSharedShoppingList(applySecurityConcerns: Boolean = false) {
        if (! listeningToList) {
            listenToListBackgroundProcess = executeOnSecondaryThread {
                val httpRequest = HttpRequestBuilder().buildNotifyOfSharedShoppingListChangesHttpRequest()
                val channel = startToListenToSharedShoppingList(httpRequest)
                channel.map { event ->
                    notifyObserver(event)
                }.collect()
            }
        }
    }

    suspend fun getProducts(): List<Product> {
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.GET,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products",
            headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
            ),
            body = "",
        )
        val response = nonBlockingHttpClient.makeRequest(httpRequest)
        val json = Json.parseToJsonElement(response.body).jsonArray
        return json.map { jsonElement ->
            Product(
                description = jsonElement.jsonObject.getValue("name").jsonPrimitive.content
            )
        }
    }

    @Throws(CancellationException::class, ThereCannotBeTwoProductsWithTheSameNameException::class)
    suspend fun addProduct(product: ProductOnServerShoppingList, applySecurityConcerns: Boolean = false) {
        val httpRequest = HttpRequestBuilder().buildAddProductHttpRequest(product)
        val response = nonBlockingHttpClient.makeRequest(httpRequest)
        if (response.statusCode in 200..299) {
            val jsonBodyResponse = Json.parseToJsonElement(response.body).jsonObject
            val errorCode = jsonBodyResponse.getValue("errorCode").jsonPrimitive.int
            val errorMessage = jsonBodyResponse.getValue("errorMessage").jsonPrimitive.content
            when (errorCode) {
                AddProductErrorCodes.PRODUCT_ALREADY_EXISTS_ON_LIST.code -> {
                    throw ThereCannotBeTwoProductsWithTheSameNameException(errorMessage)
                }
                AddProductErrorCodes.PRODUCT_EXCEEDS_MAXIMUM_LENGTH.code -> {
                    throw ProductDescriptionExceedsMaximumLengthException(errorMessage)
                }
                AddProductErrorCodes.PRODUCT_IS_SHORTER_THAN_MINIMUM_LENGTH.code -> {
                    throw ProductDescriptionIsShorterThanMinimumLengthException(errorMessage)
                }
            }
        } else {
            throw Exception("Status code: ${response.statusCode}. Response body: ${response.body}")
        }
    }

    suspend fun modifyProduct(newProduct: ProductOnServerShoppingList) {
        val httpRequest = HttpRequestBuilder().buildModifyProductHttpRequest(newProduct)
        val response = nonBlockingHttpClient.makeRequest(httpRequest)
        if (response.statusCode in 200..299) {
            val jsonBodyResponse = Json.parseToJsonElement(response.body).jsonObject
            val errorCode = jsonBodyResponse.getValue("errorCode").jsonPrimitive.int
            val errorMessage = jsonBodyResponse.getValue("errorMessage").jsonPrimitive.content
            when (errorCode) {
                ModifyProductErrorCodes.THERE_IS_ANOTHER_PRODUCT_WITH_THAT_NAME.code -> {
                    throw ThereCannotBeTwoProductsWithTheSameNameException(errorMessage)
                }
                ModifyProductErrorCodes.PRODUCT_EXCEEDS_MAXIMUM_LENGTH.code -> {
                    throw ProductDescriptionExceedsMaximumLengthException(errorMessage)
                }
                ModifyProductErrorCodes.PRODUCT_IS_SHORTER_THAN_MINIMUM_LENGTH.code -> {
                    throw ProductDescriptionIsShorterThanMinimumLengthException(errorMessage)
                }
                ModifyProductErrorCodes.PRODUCT_DOES_NOT_EXIST.code -> {
                    throw ProductDoesNotExistException(errorMessage)
                }
            }
        } else {
            throw Exception("Status code: ${response.statusCode}. Response body: ${response.body}")
        }
    }

    suspend fun deleteProduct(product: ProductOnServerShoppingList) {
        val httpRequest = HttpRequestBuilder().buildDeleteProductHttpRequest(product)
        nonBlockingHttpClient.makeRequest(httpRequest)
    }

    suspend fun deleteAllProducts() {
        val httpRequest = HttpRequestBuilder().buildDeleteAllProductsExceptHttpRequest(emptyList())
        nonBlockingHttpClient.makeRequest(httpRequest)
    }

    suspend fun stopListeningToSharedShoppingList() {
        listenToListBackgroundProcess?.cancel()
        listenToListBackgroundProcess = null
    }

    private suspend fun startToListenToSharedShoppingList(httpRequest: HttpRequest): Flow<String> {
        return streamingHttpClient.makeStreamingRequest(httpRequest)
    }

    private suspend fun notifyObserver(event: String) {
        val listOfJsonEvents = parseEventToJsonChunks(event)
        listOfJsonEvents.forEach { jsonEvent ->
            val eventType = readEventType(jsonEvent)
            when (eventType) {
                "currentState" -> {
                    notifyObserverOfCurrentListState(jsonEvent)
                }
                "addedItem" -> {
                    notifyObserverOfProductAdded(jsonEvent)
                }
                "modifiedItem" -> {
                    notifyObserverOfProductModified(jsonEvent)
                }
                "deletedItem" -> {
                    notifyObserverOfProductDeleted(jsonEvent)
                }
            }

        }
    }

    private fun readEventType(event: JsonObject): String {
        return event.getValue("typeOfNotification").jsonPrimitive.content
    }

    private fun getProductFromEvent(event: JsonObject): JsonObject {
        return event.getValue("item").jsonObject
    }

    private fun executeOnSecondaryThread(block: suspend () -> Unit): Job {
        return coroutineScopeOnMainThread.launch(Dispatchers.Default) {
            block()
        }
    }

    private suspend fun executeOnMainThread(block: () -> Unit) {
        withContext(Dispatchers.Main) {
            block()
        }
    }

    private suspend fun notifyObserverOfCurrentListState(event: JsonObject) {
        val product = getProductFromEvent(event)
        executeOnMainThread {
            sharedShoppingListObserver?.stateAtTheMomentOfSubscribing(
                adaptProduct(product)
            )
        }
    }

    private suspend fun notifyObserverOfProductAdded(event: JsonObject) {
        val product = getProductFromEvent(event)
        executeOnMainThread {
            sharedShoppingListObserver?.productAdded(
                adaptProduct(product)
            )
        }
    }

    private suspend fun notifyObserverOfProductModified(event: JsonObject) {
        val product = getProductFromEvent(event)
        executeOnMainThread {
            sharedShoppingListObserver?.productModified(
                adaptProduct(product)
            )
        }
    }

    private suspend fun notifyObserverOfProductDeleted(event: JsonObject) {
        val product = getProductFromEvent(event)
        executeOnMainThread {
            sharedShoppingListObserver?.productDeleted(
                readProductId(product)
            )
        }
    }

    private fun adaptProduct(product: JsonObject): ProductOnServerShoppingList {
        return ProductOnServerShoppingList(
            id = readProductId(product),
            description = product.getValue("text").jsonPrimitive.content,
        )
    }

    private fun readProductId(product: JsonObject): String {
        return product.getValue("id").jsonPrimitive.content
    }

    private fun parseEventToJsonChunks (ndJson: String): List<JsonObject> {
        val ndJsonSplit = splitNdJsonIntoLines(ndJson)
        val listOfJsons = removeEmptyLines(ndJsonSplit)
        return listOfJsons.map { json ->
            Json.parseToJsonElement(json).jsonObject
        }
    }

    private fun splitNdJsonIntoLines(ndJson: String): List<String> {
        return ndJson.split("\n|\r\n".toRegex())
    }

    private fun removeEmptyLines(ndJson: List<String>): List<String> {
        return ndJson.filter { line ->
            line.isNotEmpty()
        }
    }
}