package day.toons.domain.member

import day.toons.domain.common.AuditingEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table


@Table(name = "TB_MEMBER")
@Entity
class Member(
    @Column
    var email: String,
    @Column
    var username: String,

    @Column
    var encryptedPassword: String? = null,

    @Column
    var providerId: String? = null,

    provider: AuthProvider? = null,
): AuditingEntity() {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var provider: AuthProvider = provider?: AuthProvider.local


    fun update(email: String? = null, username: String? = null): Member {
        this.username = username ?: this.username
        this.email = email ?: this.email
        return this
    }

}

