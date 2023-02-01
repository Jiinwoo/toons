package day.toons.global.config.security

import day.toons.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class MemberPrincipal private constructor(
    private val id: Long?,
    private var email: String,
    private var username: String,
    private var authorities: Collection<GrantedAuthority>,
    private var attributes: MutableMap<String, Any>? = null,
    private var password: String? = null,
) : UserDetails, OAuth2User {
    override fun getName(): String {
        return this.id.toString();
    }

    override fun getAttributes(): MutableMap<String, Any>? {
        return attributes
    }

    companion object {
        fun create(member: Member, roles: Collection<GrantedAuthority>): MemberPrincipal {
            return MemberPrincipal(
                id = member.id,
                email = member.email,
                username = member.username,
                authorities = roles,
                password = member.encryptedPassword
            )
        }

        fun create(member: Member, attributes: MutableMap<String, Any>?=null): MemberPrincipal {
            return MemberPrincipal(
                id = member.id,
                email = member.email,
                username = member.username,
                authorities = listOf(SimpleGrantedAuthority("ROLE_USER")),
                password = member.encryptedPassword,
                attributes = attributes
            )
        }
    }

    fun getEmail (): String {
        return email;
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this.authorities
    }

    override fun getPassword(): String? {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true;
    }

    override fun isEnabled(): Boolean {
        return true;
    }
}
