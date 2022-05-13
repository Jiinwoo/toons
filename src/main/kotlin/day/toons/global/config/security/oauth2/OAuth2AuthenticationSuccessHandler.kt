package day.toons.global.config.security.oauth2

import day.toons.global.config.AppProperties
import day.toons.global.config.security.JwtUtil
import day.toons.global.config.security.MemberPrincipal
import day.toons.global.error.exception.BadRequestException
import day.toons.global.util.CookieUtils
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
    private val appProperties: AppProperties,
    private val jwtUtil: JwtUtil,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)
        if(response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String{
        val redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
            ?.let {
                if(!isAuthorizedRedirectUri(it.value))
                    throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
                it.value
            }?:defaultTargetUrl
        val token = jwtUtil.generateToken(authentication.principal as MemberPrincipal)
        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("token",token)
            .build().toUriString()
    }

    fun isAuthorizedRedirectUri(uri: String): Boolean =
        URI.create(uri).let { clientRedirectUri ->
            appProperties.oauth2.authorizedRedirectUris.any { authorizedRedirectUri ->

                val authorizedURI = URI.create(authorizedRedirectUri)
                authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true) && authorizedURI.port == clientRedirectUri.port
            }
        }
    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }
}