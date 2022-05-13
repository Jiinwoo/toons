package day.toons.global.config.security

import org.springframework.security.core.AuthenticationException


class PasswordNotMatchException(msg: String): AuthenticationException(msg) {
}