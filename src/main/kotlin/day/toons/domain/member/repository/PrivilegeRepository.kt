package day.toons.domain.member.repository

import day.toons.domain.member.Privilege
import org.springframework.data.repository.CrudRepository

interface PrivilegeRepository : CrudRepository<Privilege, Long> {
}
