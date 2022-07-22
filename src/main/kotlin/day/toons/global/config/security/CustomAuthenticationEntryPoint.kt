package day.toons.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = KotlinLogging.logger {}
@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("UnAuthorized!!! message : ${authException.message}")

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()

        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
    }
}