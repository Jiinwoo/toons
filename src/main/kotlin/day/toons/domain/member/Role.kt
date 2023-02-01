package day.toons.domain.member

import day.toons.domain.common.AuditingEntityId
import javax.persistence.*


@Entity
@Table(name = "TB_ROLE")
class Role(
        @Column(name = "name", unique = true) val name: String,
        @ManyToMany(mappedBy = "roles") val users: MutableSet<Member> = mutableSetOf(),
) : AuditingEntityId() {
    @ManyToMany
    @JoinTable(name = "roles_privileges",
            joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
            )
    val privileges: Set<Privilege> = emptySet()
}
