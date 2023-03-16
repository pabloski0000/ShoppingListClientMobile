package main.shoppilientmobile.userRegistrationFeature.dataSources.apis.errorCodes

enum class UserRegistrationErrorCodes(val code: Int) {
    USER_NICKNAME_TOO_SHORT(401),
    USER_NICKNAME_TOO_LONG(402),
    TWO_USERS_WITH_THE_SAME_NICKNAME(403),
    THERE_CAN_ONLY_BE_ONE_ADMIN(404),
    USER_DOES_NOT_EXIST(405),
    WRONG_USER_NICKNAME_OR_PASSWORD(406);
}