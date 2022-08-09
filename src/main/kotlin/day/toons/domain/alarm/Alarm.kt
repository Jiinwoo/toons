package day.toons.domain.alarm

import day.toons.domain.common.AuditingEntity
import day.toons.domain.member.Member
import day.toons.domain.webtoon.Webtoon
import javax.persistence.*

@Table(name = "TB_ALARM")
@Entity
class Alarm(
    webtoon: Webtoon,
    member: Member
) : AuditingEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false, referencedColumnName = "id")
    var webtoon = webtoon
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, referencedColumnName = "id")
    var member = member
        protected set

}