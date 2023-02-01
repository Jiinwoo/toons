package day.toons.domain.member.repository

import day.toons.domain.member.RoleHierarchy
import org.springframework.data.repository.CrudRepository

interface RoleHierarchyRepository: CrudRepository<RoleHierarchy, Long>{
    fun findByChildNameAndParentName(childName: String, parentName: String?): RoleHierarchy?
}
