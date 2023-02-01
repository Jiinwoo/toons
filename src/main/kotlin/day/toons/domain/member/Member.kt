package day.toons.domain.member

import day.toons.domain.common.AuditingEntity
import javax.persistence.*


@Table(
        name = "TB_MEMBER",
        indexes = [
            Index(name = "MEMBER_INDX_0", columnList = "email"),
        ]
)
@Entity
class Member(
        @Column
        var email: String,
        @Column
        var username: String,

        @Column
        var phoneNumber: String? = null,

        @Column
        var encryptedPassword: String? = null,

        @Column
        var providerId: String? = null,

        provider: AuthProvider? = null,
) : AuditingEntity() {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var provider: AuthProvider = provider ?: AuthProvider.local

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = [JoinColumn(name = "member_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    val roles: MutableSet<Role> = mutableSetOf()


    fun update(email: String? = null, username: String? = null, phoneNumber: String? = null): Member {
        this.username = username ?: this.username
        this.email = email ?: this.email
        this.phoneNumber = phoneNumber ?: this.phoneNumber
        return this
    }

}

