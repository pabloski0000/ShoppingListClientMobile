package main.shoppilientmobile.registrationFeature

import main.shoppilientmobile.domain.UserImpl
import main.shoppilientmobile.domain.domainExposure.UserRole
import kotlin.test.Test
import kotlin.test.assertTrue

class UserBusinessRulesTest {
    private lateinit var user: UserImpl

    @Test
    fun theNicknameNeedsToHaveBetweenThreeAndTwentyCharacters() {
        assertUserThrowsExceptionIfNicknameIsShorterThanThree()
        assertUserThrowsExceptionIfNicknameIsLongerThanTwenty()
    }

    private fun assertUserThrowsExceptionIfNicknameIsShorterThanThree() {
        val assertionExceptionMessage = "User cannot have a nickname shorter then three characters"
        try {
            UserImpl("", UserRole.BASIC)
            assertTrue(assertionExceptionMessage) {
                false
            }
        } catch (e: Exception) {}
        try {
            UserImpl("p", UserRole.BASIC)
            assertTrue(assertionExceptionMessage) {
                false
            }
        } catch (e: Exception) {}
        try {
            UserImpl("pa", UserRole.BASIC)
            assertTrue(assertionExceptionMessage) {
                false
            }
        } catch (e: Exception) {}
    }

    private fun assertUserThrowsExceptionIfNicknameIsLongerThanTwenty() {
        val assertionExceptionMessage = "User cannot have a nickname longer than twenty characters"
        try {
            UserImpl("abcdefghijklmnopqrstu", UserRole.BASIC)
            assertTrue(assertionExceptionMessage) {
                false
            }
        } catch (e: Exception) {}
    }
}