package day.toons.domain.member.repository

import day.toons.domain.member.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Role, Long> {
    fun findByName(name: String): Role?
}
