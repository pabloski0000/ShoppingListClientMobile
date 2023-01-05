package main.shoppilientmobile.registrationFeature.apis

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.core.builders.AsynchronousHttpClientBuilder
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.UserApiImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class UserApiRequestsTest {
    private lateinit var userApi: UserApiImpl
    val messageInformingOfStrangeBehaviourInMockEngine = "assert request headers are correct" +
            " cannot be done due to a strange behaviour in ktor library"

    @Test
    fun assertUserAsAdminRegistrationRequestIsCorrect(){
        val mockEngine = MockEngine{ request ->
            //TODO(messageInformingOfStrangeBehaviourInMockEngine)
            assertJsonBodyIsCorrect(request.body, getUserRegistrationJsonRequestFormat())
            respond(
                status = HttpStatusCode.Created,
                headers = headersOf("Content-Type", "application/json"),
                content = "{\"accessToken\":\"jkalsdjflkasd.asjlkdfjaslkdjf.sdjflasjñdl\"}",
            )
        }
        userApi = UserApiImpl(
            AsynchronousHttpClientBuilder().withMockEngine(mockEngine).build()
        )
        runBlocking {
            userApi.registerAdminUser(UserBuilderImpl().setRole(UserRole.ADMIN).build())
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
        userApi = UserApiImpl(
            AsynchronousHttpClientBuilder().withMockEngine(mockEngine).build()
        )
        runBlocking {
            userApi.registerBasicUser(UserBuilderImpl().build())
        }
    }

    private fun getUserRegistrationJsonRequestFormat(): Regex{
        return """
            ^\{(\n)?( )*"nickName":( )?"\w{0,15}"(\n)?}${'$'}
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