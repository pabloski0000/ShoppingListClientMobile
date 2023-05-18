package main.shoppilientmobile.core.remote

import main.shoppilientmobile.core.storage.SecurityTokenKeeper

class UserApi(
    private val nonBlockingHttpClientImpl: NonBlockingHttpClientImpl,
    private val securityTokenKeeper: SecurityTokenKeeper,
) {
    suspend fun deleteMyself() {
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.DELETE,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/delete-myself",
            headers = mapOf(
                "Authorization" to "Bearer ${securityTokenKeeper.getSecurityToken2()}",
            ),
            body = "",
        )
        nonBlockingHttpClientImpl.makeRequest(httpRequest)
    }
}