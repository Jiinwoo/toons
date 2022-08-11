package day.toons.domain.member.exception

import day.toons.global.error.exception.ErrorCode
import day.toons.global.error.exception.InvalidValueException

class CertificationException(email: String): InvalidValueException(email, ErrorCode.EMAIL_DUPLICATION) {
}