package main.shoppilientmobile.core.remote

import main.shoppilientmobile.domain.Product
import main.shoppilientmobile.shoppingList.infrastructure.repositories.ProductOnServerShoppingList

class HttpRequestBuilder {

    fun buildNotifyOfSharedShoppingListChangesHttpRequest(): HttpRequest {
        return HttpRequest(
            httpMethod = HttpMethod.GET,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products/synchronise-with-shopping-list",
            headers = mapOf(
                "Accept" to "application/x-ndjson",
            ),
            body = "",
        )
    }

    fun buildAddProductHttpRequest(product: ProductOnServerShoppingList): HttpRequest {
        return HttpRequest(
            httpMethod = HttpMethod.POST,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products",
            headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
            ),
            body = """
                |{
                |   "name": "${product.description}"
                |}
            """.trimMargin(),
        )
    }

    fun buildModifyProductHttpRequest(newProduct: ProductOnServerShoppingList): HttpRequest {
        return HttpRequest(
            httpMethod = HttpMethod.PUT,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products/${newProduct.id}",
            headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
            ),
            body = """
                |{
                |   "name": "${newProduct.description}"
                |}
            """.trimMargin(),
        )
    }

    fun buildDeleteProductHttpRequest(product: ProductOnServerShoppingList): HttpRequest {
        return HttpRequest(
            httpMethod = HttpMethod.DELETE,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products/${product.id}",
            headers = mapOf(
                "Accept" to "application/json",
            ),
            body = "",
        )
    }

    fun buildDeleteAllProductsExceptHttpRequest(productsThatMustNotBeDeleted: List<Product>): HttpRequest {
        return HttpRequest(
            httpMethod = HttpMethod.DELETE,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/products",
            headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
            ),
            body = """
                {
                    "ids": [${productsThatMustNotBeDeleted.joinToString()}]
                }
            """.trimIndent(),
        )
    }
}