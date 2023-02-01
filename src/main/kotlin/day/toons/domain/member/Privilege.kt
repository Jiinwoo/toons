package day.toons.domain.member

import day.toons.domain.common.AuditingEntityId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "TB_PRIVILEGE")
class Privilege(
        @Column(name = "name")
        val name: String
) : AuditingEntityId() {
    @ManyToMany(mappedBy = "privileges")
    val roles: Set<Role> = emptySet()
}
