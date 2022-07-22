package day.toons.domain.alarm

import day.toons.domain.common.AuditingEntity
import day.toons.domain.member.Member
import javax.persistence.*

@Table(name = "TB_ALARM")
@Entity
class Alarm(
    webtoonName: String,
    member: Member
) : AuditingEntity() {
    @Column(nullable = false)
    var webtoonName = webtoonName
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member = member
        protected set

}