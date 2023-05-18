package main.shoppilientmobile.registrationFeature.apis

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.core.remote.HttpMethod
import main.shoppilientmobile.registrationFeature.apis.mocks.NonBlockingHttpClientMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserApiRequestsTest {

    private lateinit var httpClient: NonBlockingHttpClientMock
    val messageInformingOfStrangeBehaviourInMockEngine = "assert request headers are correct" +
            " cannot be done due to a strange behaviour in ktor library"
    private val httpsProtocol = "https"
    private val host = "lista-de-la-compra-pabloski.herokuapp.com"

    @BeforeTest
    fun setUp() {
        httpClient = NonBlockingHttpClientMock()

    }

    @Test
    fun assertUserAsAdminRegistrationRequestIsCorrect(){
        val sentRequest = httpClient.lastRequest
        assertEquals(HttpMethod.POST, sentRequest.httpMethod)
        assertEquals("$httpsProtocol://$host/api/users/register-user-admin", sentRequest.url)
        assertEquals(
            mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
            ),
            sentRequest.headers
        )
        assertTrue("Request body did not match with expected regex") {
            sentRequest.body.matches(getUserRegistrationJsonRequestFormat())
        }
    }

    @Test
    fun assertNormalUserRegistrationRequestIsCorrect(){
        val mockEngine = MockEngine{ request ->
            //TODO(messageInformingOfStrangeBehaviourInMockEngine)
            assertJsonBodyIsCorrect(request.body, getUserRegistrationJsonRequestFormat())
            respond(
                content = "",
                status = HttpStatusCode.NoContent,
                headers = headersOf("Content-Type", "application/json"),
            )
        }
        runBlocking {
            //userApi.registerBasicUser(UserBuilderImpl().build())
        }
    }

    private fun getUserRegistrationJsonRequestFormat(): Regex{
        return """
            ^\{(\n)?( )*"nickname":( )?"\w{0,15}"(\n)?}${'$'}
        """.trimIndent().toRegex()
    }

    private fun assertJsonBodyIsCorrect(requestBody: OutgoingContent, jsonFormat: Regex){
        val bodyAsString = adaptBodyToString(requestBody)
        assertTrue {
            bodyAsString.matches(jsonFormat)
        }
    }

    private fun adaptBodyToString(requestBody: OutgoingContent): String{
        return runBlocking { requestBody.toByteReadPacket().readText() }
    }
}