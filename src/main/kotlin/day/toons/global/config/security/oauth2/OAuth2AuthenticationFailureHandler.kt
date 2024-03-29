package day.toons.global.config.security.oauth2

import day.toons.global.util.CookieUtils
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URLEncoder
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
): SimpleUrlAuthenticationFailureHandler (){
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        var targetUrl = CookieUtils.getCookie(request,
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
        )?.value?:"/"

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", URLEncoder.encode(exception.localizedMessage, "UTF-8"))
            .build().toUriString()

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}