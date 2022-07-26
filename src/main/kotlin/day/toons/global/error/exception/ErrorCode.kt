package day.toons.global.error.exception

enum class ErrorCode(
    var status: Int,
    var code: String,
    var message: String,
) {
    // Common

    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "METHOD_NOT_ALLOWED"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    AUTHENTICATION_REQUIRED(401, "C007", "Authentication Required"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "이메일이 중복되었습니다."),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

    // Coupon
    COUPON_ALREADY_USE(400, "CO001", "Coupon was already used"),
    COUPON_EXPIRE(400, "CO002", "Coupon was already expired")
}
