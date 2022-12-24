package startWithExceptions

class UserNickNameValidationException(
    message: String
): RuntimeException(message) {
}