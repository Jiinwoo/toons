package day.toons.global.config.security.oauth2

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException(msg: String): AuthenticationException(msg)