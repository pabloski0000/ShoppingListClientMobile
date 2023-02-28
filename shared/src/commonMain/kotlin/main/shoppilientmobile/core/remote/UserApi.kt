package main.shoppilientmobile.core.remote

import main.shoppilientmobile.core.storage.SecurityTokenKeeper

class UserApi(
    private val asynchronousHttpClientImpl: AsynchronousHttpClientImpl,
    private val securityTokenKeeper: SecurityTokenKeeper,
) {
    fun deleteMyself() {
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.DELETE,
            url = "https://lista-de-la-compra-pabloski.herokuapp.com/api/users/delete-myself",
            headers = mapOf(
                "Authorization" to "Bearer ${securityTokenKeeper.getSecurityToken2()}",
            ),
            body = "",
        )
        asynchronousHttpClientImpl.makeRequest2(httpRequest)
    }
}