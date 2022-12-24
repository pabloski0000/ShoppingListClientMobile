package unitTests

import CustomHttpClient
import httpBodyStrucutres.JsonStructure
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequestsTest {
    var customHttpClient: CustomHttpClient = CustomHttpClient()
    val messageInformingOfStrangeBehaviourInMockEngine = "assert request headers are correct" +
            " cannot be done due to a strange behaviour in ktor library"
    @Test
    fun assertAdminRegistrationRequestIsCorrect(){
        val mockEngine = MockEngine{ request ->
            TODO(messageInformingOfStrangeBehaviourInMockEngine)
            assertJsonBodyIsCorrect(request.body, getAdminRegistrationJsonRequestFormat())
            respond(
                content = "field not relevant for this test",
            )
        }
        customHttpClient = CustomHttpClient(mockEngine)
    }

    private fun getAdminRegistrationJsonRequestFormat(): Regex{
        return """
            ^\{"nickName":( )?"\w{0,15}"}$
        """.trimIndent().toRegex()
    }

    private fun getAdminRegistrationJsonResponseFormat(): Regex{
        return """
            ^\{"accessToken":( )?"\w{0,15}"}$
        """.trimIndent().toRegex()
    }

    @Test
    fun assertProductAdditionRequestIsCorrect(){
        val expectedResponse = JsonStructure.Product("123465789", "hamburger")
        val mockEngine = MockEngine{ request ->
            TODO(messageInformingOfStrangeBehaviourInMockEngine)
            assertJsonBodyIsCorrect(request.body, getProductAdditionJsonFormat())
            respond(
                content = """{"id": "${expectedResponse.id}", "name": "${expectedResponse.name}"}""",
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        customHttpClient = CustomHttpClient(mockEngine)

        val httpResponse = customHttpClient.addProduct(
            requestBody = JsonStructure.ProductAddition(""),
            accessToken = "any"
        )
        assertEquals(expectedResponse.id,httpResponse.id)
        assertEquals(expectedResponse.name,httpResponse.name)
    }

    private fun assertJsonBodyIsCorrect(requestBody: OutgoingContent, jsonFormat: Regex){
        val bodyAsString = adaptBodyToString(requestBody)
        assertTrue {
            bodyAsString.matches(jsonFormat)
        }
    }

    private fun getProductAdditionJsonFormat(): Regex{
        return """
            ^\{"name":( )?"\w{0,50}"}$
        """.trimIndent().toRegex()
    }

    private fun adaptBodyToString(requestBody: OutgoingContent): String{
        return runBlocking { requestBody.toByteReadPacket().readText() }
    }

}