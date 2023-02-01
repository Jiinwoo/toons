package day.toons.global.config.security


import io.jsonwebtoken.*
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


private val logger = KotlinLogging.logger {}

@Component
class JwtUtil {

    companion object {
        const val TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2
        const val REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2
        const val ACCESS_TOKEN_NAME = "accessToken"
        const val REFRESH_TOKEN_NAME = "refreshToken"
    }

    @Value("\${spring.jwt.secret}")
    lateinit var SECRET_KEY: String


    fun getEmail(token: String): String {
        return getClaimsFromToken(token)["email"] as String
    }

    fun generateToken(principal: MemberPrincipal): String {
        return generateJwtToken(principal, TOKEN_VALIDATION_SECOND)
    }

    fun generateRefreshToken(principal: MemberPrincipal): String {
        return generateJwtToken(principal, REFRESH_TOKEN_VALIDATION_SECOND)
    }

    fun isValidToken(token: String): Boolean {
        try {
            val claims = getClaimsFromToken(token)
            logger.info("expireTime ${claims.expiration}")
            logger.info("email ${claims.get("email")}")
            return true;
        } catch (exception: ExpiredJwtException) {
            logger.error("Token Expired")
            return false;
        } catch (exception: JwtException) {
            logger.error("Token Tampered")
            return false;
        } catch (exception: NullPointerException) {
            logger.error("Tokens is null")
            return false;
        }
    }

    private fun generateJwtToken(principal: MemberPrincipal, expireTime: Long): String {
        return Jwts.builder()
                .setSubject(principal.getEmail())
                .setClaims(createClaims(principal))
                .setExpiration(Date(System.currentTimeMillis() + expireTime))
                .setIssuedAt(Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, getSigningKey(SECRET_KEY))
                .compact()
    }

    private fun createClaims(principal: MemberPrincipal): Claims? {
        val claims = Jwts.claims()
        claims.putIfAbsent("email", principal.getEmail())
        claims.putIfAbsent("roles", principal.authorities.map { it.authority })
        return claims
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts
                .parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token).body
    }

    private fun getSigningKey(secretKey: String): Key? {
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey)
        return SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.jcaName)
    }

}
