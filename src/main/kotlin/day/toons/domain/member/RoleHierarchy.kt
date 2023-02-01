package day.toons.domain.member

import day.toons.domain.common.AuditingEntityId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "TB_ROLE_HIERARCHY")
class RoleHierarchy(
        @Column(name = "child_name", nullable = false)
        val childName: String,
        @Column(name = "parent_name", nullable = true)
        val parentName: String?
) : AuditingEntityId()
