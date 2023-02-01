package day.toons

import day.toons.domain.member.Role
import day.toons.domain.member.RoleHierarchy
import day.toons.domain.member.repository.MemberRepository
import day.toons.domain.member.repository.RoleHierarchyRepository
import day.toons.domain.member.repository.RoleRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Profile("!test")
@Component
class TempSetup(
        private val memberRepository: MemberRepository,
        private val roleRepository: RoleRepository,
        private val roleHierarchyRepository: RoleHierarchyRepository,
) : ApplicationListener<ContextRefreshedEvent> {

    var setup: Boolean = false

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (setup) return
        // hierarchy
        createRoleHierarchyIfNotExist("ROLE_ADMIN", null)
        createRoleHierarchyIfNotExist("ROLE_USER", "ROLE_ADMIN")

        // ROLE_ADMIN, ROLE_UER 추가
        val adminRole = createRoleIfNotExist("ROLE_ADMIN")
        val userRole = createRoleIfNotExist("ROLE_UER")
        // jwjung5038@gmail.com 계정에 ADMIN_ROLE 연결
        val member = memberRepository.findByEmail("jwjung5038@gmail.com").get()
        member.roles.add(adminRole)
        memberRepository.save(member)
        setup = true
    }

    private fun createRoleHierarchyIfNotExist(childName: String, parentName: String?) {
        if(roleHierarchyRepository.findByChildNameAndParentName(childName, parentName) == null) {
            roleHierarchyRepository.save(RoleHierarchy(childName, parentName))
        }
    }
    private fun createRoleIfNotExist(name: String): Role {
        return roleRepository.findByName(name) ?: roleRepository.save(Role(name = name))
    }
}
