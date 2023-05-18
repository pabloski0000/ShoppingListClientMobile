package main.shoppilientmobile.userRegistrationFeature.dataSources.apis

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import main.shoppilientmobile.application.UserBuilderImpl
import main.shoppilientmobile.core.storage.SecurityTokenKeeper
import main.shoppilientmobile.core.remote.*
import main.shoppilientmobile.domain.domainExposure.User
import main.shoppilientmobile.domain.domainExposure.UserRole
import main.shoppilientmobile.domain.exceptions.*
import main.shoppilientmobile.shoppingList.application.UserRegistrationsListener
import main.shoppilientmobile.userRegistrationFeature.dataSources.RegistrationRemoteDataSource
import main.shoppilientmobile.userRegistrationFeature.dataSources.apis.errorCodes.UserRegistrationErrorCodes
import main.shoppilientmobile.userRegistrationFeature.entities.Registration
import main.shoppilientmobile.userRegistrationFeature.entities.RegistrationCode
import main.shoppilientmobile.userRegistrationFeature.useCases.exceptions.WrongCodeException
import kotlin.coroutines.cancellation.CancellationException

class RegistrationApi(
    private val httpClient: NonBlockingHttpClient,
    private val streamingHttpClient: StreamingHttpClient,
    private val securityTokenKeeper: SecurityTokenKeeper,
): ShoppingListServerApi(), RegistrationRemoteDataSource {
    private val userBuilder = UserBuilderImpl()

    override suspend fun registerAdmin(registration: Registration): User {
        signUpAdmin(registration)
        return createUser(registration)
    }

    override suspend fun registerUser(registration: Registration) {
        signUpUser(registration)
    }

    private suspend fun signUpUser(registration: Registration) {
        val url = "$protocolAndHost/api/users/register-user"
        val headers = mapOf(
            "Content-Type" to "application/json",
        )
        val body = """
            {"nickname": "${registration.nickname}"}
        """.trimIndent()
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = headers,
            body = body,
        )
        val response = httpClient.makeRequest(httpRequest)
        if (response.statusCode in 200..299) {
            val jsonBodyResponse = Json.parseToJsonElement(response.body).jsonObject
            val errorCode = jsonBodyResponse.getValue("errorCode").jsonPrimitive.int
            val errorMessage = jsonBodyResponse.getValue("errorMessage").jsonPrimitive.content
            when (errorCode) {
                UserRegistrationErrorCodes.USER_NICKNAME_TOO_LONG.code -> {
                    throw UserNicknameTooLongException(errorMessage)
                }
                UserRegistrationErrorCodes.USER_NICKNAME_TOO_SHORT.code -> {
                    throw UserNicknameTooShortException(errorMessage)
                }
                UserRegistrationErrorCodes.TWO_USERS_WITH_THE_SAME_NICKNAME.code -> {
                    throw ThereCannotBeTwoUsersWithTheSameNicknameException(errorMessage)
                }
            }
        } else {
            throw Exception("Status code: ${response.statusCode}. Response body: ${response.body}")
        }
    }

    private suspend fun signUpAdmin(registration: Registration) {
        val url = "$protocolAndHost/api/users/register-user-admin"
        val headers = mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
        )
        val body = """
            {"nickname": "${registration.nickname}"}
        """.trimIndent()
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = url,
            headers = headers,
            body = body,
        )
        val response = httpClient.makeRequest(httpRequest)
        if (response.statusCode in 200..299) {
            val jsonBodyResponse = Json.parseToJsonElement(response.body).jsonObject
            val errorCode = jsonBodyResponse.getValue("errorCode").jsonPrimitive.int
            val errorMessage = jsonBodyResponse.getValue("errorMessage").jsonPrimitive.content
            when (errorCode) {
                UserRegistrationErrorCodes.THERE_CAN_ONLY_BE_ONE_ADMIN.code -> {
                    throw ThereCanOnlyBeOneAdmin(errorMessage)
                }
                UserRegistrationErrorCodes.USER_NICKNAME_TOO_LONG.code -> {
                    throw UserNicknameTooLongException(errorMessage)
                }
                UserRegistrationErrorCodes.USER_NICKNAME_TOO_SHORT.code -> {
                    throw UserNicknameTooShortException(errorMessage)
                }
            }
        } else {
            throw Exception("Status code: ${response.statusCode}. Response body: ${response.body}")
        }
        saveSecurityToken(response)
    }

    @Throws(CancellationException::class, WrongCodeException::class)
    override suspend fun confirmUserRegistration(registration: Registration): User {
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.POST,
            url = "$protocolAndHost/api/users/confirm-registration",
            headers = mapOf("Content-Type" to "application/json"),
            body = """
                {
                    "nickname": "${registration.nickname}",
                    "code": ${registration.signature!!.value}
                }
            """.trimIndent(),
        )
        val response = httpClient.makeRequest(httpRequest)
        if (response.statusCode in 200..299) {
            val jsonBodyResponse = Json.parseToJsonElement(response.body).jsonObject
            val errorCode = jsonBodyResponse.getValue("errorCode").jsonPrimitive.int
            val errorMessage = jsonBodyResponse.getValue("errorMessage").jsonPrimitive.content
            when (errorCode) {
                UserRegistrationErrorCodes.USER_DOES_NOT_EXIST.code -> {
                    throw UserDoesNotExistException(errorMessage)
                }
                UserRegistrationErrorCodes.USER_NICKNAME_TOO_LONG.code -> {
                    throw UserNicknameTooLongException(errorMessage)
                }
                UserRegistrationErrorCodes.USER_NICKNAME_TOO_SHORT.code -> {
                    throw UserNicknameTooShortException(errorMessage)
                }
                UserRegistrationErrorCodes.TWO_USERS_WITH_THE_SAME_NICKNAME.code -> {
                    throw ThereCannotBeTwoUsersWithTheSameNicknameException(errorMessage)
                }
                UserRegistrationErrorCodes.WRONG_USER_NICKNAME_OR_PASSWORD.code -> {
                    throw WrongUserNicknameOrPasswordException(errorMessage)
                }
            }
        } else {
            throw Exception("Status code: ${response.statusCode}. Response body: ${response.body}")
        }
        saveSecurityToken(response)
        return createUser(registration)
    }

    override suspend fun listenToUserRegistrations(userRegistrationListener: UserRegistrationsListener) {
        val url = "$protocolAndHost/api/users/alert-admin-to-confirm-registration"
        val headers = mapOf(
            "Accept" to "application/x-ndjson",
            "Authorization" to "Bearer ${securityTokenKeeper.getSecurityToken()}",
        )
        val httpRequest = HttpRequest(
            httpMethod = HttpMethod.GET,
            url = url,
            headers = headers,
            body = "",
        )
        val response = streamingHttpClient.makeStreamingRequest(httpRequest)
        response.map { responseChunk ->
            val jsonResponse = Json.parseToJsonElement(responseChunk).jsonObject
            val nickname = jsonResponse.getValue("nickname").jsonPrimitive.content
            val code = jsonResponse.getValue("code").jsonPrimitive.int
            userRegistrationListener.userRegistered(
                Registration(nickname, UserRole.BASIC, RegistrationCode(code))
            )
        }.collect()
    }

    private suspend fun saveSecurityToken(response: HttpResponse) {
        val responseBody = response.body
        val json = Json.parseToJsonElement(responseBody).jsonObject
        val securityToken = json.getValue("accessToken").jsonPrimitive.content
        securityTokenKeeper.setSecurityToken(securityToken)
    }

    private fun createUser(registration: Registration): User {
        return userBuilder.giveItANickname(registration.nickname).setRole(registration.role).build()
    }
}