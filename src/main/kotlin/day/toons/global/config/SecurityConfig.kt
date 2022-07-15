package day.toons.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import day.toons.global.config.security.*
import day.toons.global.config.security.oauth2.CustomOAuth2UserService
import day.toons.global.config.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import day.toons.global.config.security.oauth2.OAuth2AuthenticationFailureHandler
import day.toons.global.config.security.oauth2.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CharacterEncodingFilter

@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val objectMapper: ObjectMapper,
    private val customerUserDetailsService: CustomUserDetailsService,
    private val customerAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val customOAuth2UserService : CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val OAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler
) : WebSecurityConfigurerAdapter() {
    companion object {
        private val AUTH_WHITELIST = arrayOf(
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            // other public endpoints of your API may be appended to this array
            "/api/webtoons"
        )
    }
    @Bean
    fun getCustomAuthenticationProvider(passwordEncoder: PasswordEncoder): CustomAuthenticationProvider {
        return CustomAuthenticationProvider(passwordEncoder, customerUserDetailsService)
    }

    override fun configure(http: HttpSecurity) {
        val filter = CharacterEncodingFilter()
        filter.encoding = "UTF-8"
        filter.setForceEncoding(true)
        http
            .addFilterBefore(filter, CsrfFilter::class.java)
            .httpBasic().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(*AUTH_WHITELIST).permitAll()
            .antMatchers("/oauth2/**").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/auth").permitAll()
            .antMatchers(HttpMethod.POST,"/api/members").permitAll()
            .antMatchers(HttpMethod.OPTIONS,"/api/members").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorization")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
//            .redirectionEndpoint()
//            .baseUri("/oauth2/callback/*")
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(OAuth2AuthenticationFailureHandler)
        http
            .addFilter(getJwtAuthenticationFilter())
            .addFilter(getJwtAuthorizationFilter())
            .exceptionHandling()
            .authenticationEntryPoint(customerAuthenticationEntryPoint)
    }


    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun cookieAuthorizationRequestRepository(): HttpCookieOAuth2AuthorizationRequestRepository =
        HttpCookieOAuth2AuthorizationRequestRepository()

    @Bean
    fun getJwtAuthenticationFilter(): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter(objectMapper)
        filter.setFilterProcessesUrl("/auth")
        filter.setAuthenticationManager( authenticationManager())
        filter.setAuthenticationFailureHandler(getSecurityHandler())
        filter.setAuthenticationSuccessHandler(getSecurityHandler())
        filter.afterPropertiesSet()
        return filter
    }
    @Bean
    fun getJwtAuthorizationFilter(): JwtAuthorizationFilter {
        return JwtAuthorizationFilter(authenticationManager(), customerUserDetailsService, jwtUtil)
    }

    @Bean
    fun getSecurityHandler (): SecurityHandler {
        return SecurityHandler(objectMapper, jwtUtil)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.addAllowedOrigin("*")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }


}