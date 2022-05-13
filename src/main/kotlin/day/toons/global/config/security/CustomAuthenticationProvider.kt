package day.toons.global.config.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

class CustomAuthenticationProvider(
    private val passwordEncoder: PasswordEncoder,
    private val customUserDetailsService: CustomUserDetailsService,
): AbstractUserDetailsAuthenticationProvider() {
    override fun additionalAuthenticationChecks(
        userDetails: UserDetails?,
        authentication: UsernamePasswordAuthenticationToken?
    ) {
        if (authentication!!.credentials == null) {
            logger.debug("Failed to authenticate since no credentials provided")
            throw BadCredentialsException(
                messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials")
            )
        }
        val password = authentication.credentials.toString()
        if(!passwordEncoder.matches(password, userDetails?.password)) {
            throw BadCredentialsException(
                messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials")
            )
        }
    }

    override fun retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken?): UserDetails {
        try {
            val loadedUser: UserDetails = customUserDetailsService.loadUserByUsername(username)
            return loadedUser
        } catch (ex: UsernameNotFoundException) {
            throw ex;
        } catch (ex: InternalAuthenticationServiceException) {
            throw ex;
        } catch (ex: Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex)
        }

    }
}