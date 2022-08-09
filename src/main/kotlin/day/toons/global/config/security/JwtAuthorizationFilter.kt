package day.toons.global.config.security

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val customerUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
): BasicAuthenticationFilter(authenticationManager){
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response)
            return
        }
        val authentication: Authentication? = getUsernamePasswordAuthentication(header)
        if(authentication != null ){
            SecurityContextHolder.getContext().authentication = authentication
            chain.doFilter(request, response)
        }else {
            chain.doFilter(request,response)
        }
    }

    private fun getUsernamePasswordAuthentication(header: String): Authentication? {
        val token = header.replace("Bearer", "")
        try {
            val email = jwtUtil.getEmail(token)
            val userDetails: UserDetails = customerUserDetailsService.loadUserByUsername(email)
            return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        } catch(e: ExpiredJwtException) {
            logger.error(e)
            return null
        } catch (e: Exception) {
            logger.error(e)
            return null
        }
    }

}