package day.toons.global.config.security

import day.toons.domain.member.repository.MemberRepository
import day.toons.domain.member.repository.RoleHierarchyRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional
@Service
class CustomUserDetailsService(
        private val memberRepository: MemberRepository,
        private val roleHierarchyRepository: RoleHierarchyRepository
) :UserDetailsService{

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByEmail(username).orElseThrow {
            UsernameNotFoundException("해당하는 유저가 없습니다. : ${username}")
        }
        val roleHierarchies = roleHierarchyRepository.findAll()

        val rolesToVisitSet = member.roles.map { it.name }.toMutableSet()
        val visitedRolesSet = mutableSetOf<String>()
        val mapRoles: MutableMap<String, Set<GrantedAuthority>> = mutableMapOf()
        while(rolesToVisitSet.isNotEmpty()) {
            val role = rolesToVisitSet.iterator().next()
            rolesToVisitSet.remove(role)
            if (visitedRolesSet.contains(role)) {
                continue
            }
            rolesToVisitSet.addAll(roleHierarchies.filter { it.parentName == role }.map { it.childName })
            visitedRolesSet.add(role)
        }



        return MemberPrincipal.create(member, visitedRolesSet.map { SimpleGrantedAuthority(it) })
    }
}
