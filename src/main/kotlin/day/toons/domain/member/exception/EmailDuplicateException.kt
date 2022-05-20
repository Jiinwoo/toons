package day.toons.domain.member.exception

import day.toons.global.error.exception.ErrorCode
import day.toons.global.error.exception.InvalidValueException

class EmailDuplicateException(email: String): InvalidValueException(email, ErrorCode.EMAIL_DUPLICATION) {
}