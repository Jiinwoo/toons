package day.toons.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.PrintWriter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap


class SecurityHandler(
    private val objectMapper: ObjectMapper,
    private val jwtUtil: JwtUtil
) : AuthenticationSuccessHandler, AuthenticationFailureHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val principal = authentication.principal as MemberPrincipal
        val token = jwtUtil.generateToken(principal)
        val data = mutableMapOf<String, String>(
            "token" to token
        )
        response.outputStream
            .println(objectMapper.writeValueAsString(data))
    }

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val data: MutableMap<String, Any?> = HashMap()
        data["status"] = 401
        data["errors"] = Collections.EMPTY_LIST
        data["message"] = "아이디나 패스워드를 확인해주세요."
        val writeValueAsString = objectMapper.writeValueAsString(data)
        val out: PrintWriter = response.writer
        out.print(writeValueAsString)
        out.flush()
    }
}