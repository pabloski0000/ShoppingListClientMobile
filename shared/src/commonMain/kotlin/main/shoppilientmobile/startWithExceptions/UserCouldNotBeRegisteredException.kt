package startWithExceptions

class UserCouldNotBeRegisteredException(
    message: String
): RuntimeException(message) {
}