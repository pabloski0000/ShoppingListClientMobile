package main.shoppilientmobile.unitTests.infrastructure.repositories.apiServer

import ServerRequests
import main.shoppilientmobile.httpBodyStructures.JsonStructure
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.domain.domainExposure.UserRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequestsTest {
    var customHttpClient: ServerRequests = ServerRequests()
    val messageInformingOfStrangeBehaviourInMockEngine = "assert request headers are correct" +
            " cannot be done due to a strange behaviour in ktor library"
    @Test
    fun assertUserAsAdminRegistrationRequestIsCorrect(){
        val mockEngine = MockEngine{ request ->
            //TODO(messageInformingOfStrangeBehaviourInMockEngine)
            assertJsonBodyIsCorrect(request.body, getUserRegistrationJsonRequestFormat())
            respond(
                content = "{\"accessToken\":\"jkalsdjflkasd.asjlkdfjaslkdjf.sdjflasjñdl\"}",
                headers = headersOf("Content-Type", "application/json")
            )
        }
        customHttpClient = ServerRequests(mockEngine)
        customHttpClient.registerAdminUser(UserBuilderImpl().setRole(UserRole.ADMIN).build())
    }

    private fun getUserRegistrationJsonRequestFormat(): Regex{
        return """
            ^\{(\n)?( )*"nickName":( )?"\w{0,15}"(\n)?}${'$'}
        """.trimIndent().toRegex()
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
        customHttpClient = ServerRequests(mockEngine)
        customHttpClient.registerBasicUser(UserBuilderImpl().build())
    }

    @Test
    fun assertProductAdditionRequestIsCorrect(){
        val expectedResponse = JsonStructure.Product("123465789", "hamburger")
        val mockEngine = MockEngine{ request ->
            //TODO(messageInformingOfStrangeBehaviourInMockEngine)
            assertJsonBodyIsCorrect(request.body, getProductAdditionJsonFormat())
            respond(
                content = """{"id": "${expectedResponse.id}", "name": "${expectedResponse.name}"}""",
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        customHttpClient = ServerRequests(mockEngine)

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
            ^\{(\n)?( )*"name":( )?"\w{0,50}"(\n)?}${'$'}
        """.trimIndent().toRegex()
    }

    private fun adaptBodyToString(requestBody: OutgoingContent): String{
        return runBlocking { requestBody.toByteReadPacket().readText() }
    }

}