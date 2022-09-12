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
    SMS_FAIL(400, "M002", "SMS 발송 실패"),
    CODE_NOT_FOUND(400, "M003", "SMS 인증 시간이 만료됐거나 존재하지 않습니다."),
    CODE_NOT_EQUAL(400, "M004", "인증 코드가 다릅니다."),

    // Coupon
    COUPON_ALREADY_USE(400, "CO001", "Coupon was already used"),
    COUPON_EXPIRE(400, "CO002", "Coupon was already expired"),

    // Webtoon
    CRAWLER_FAIL(400, "W001", "크롤링 실패"),
}
