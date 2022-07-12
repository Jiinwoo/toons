package day.toons.domain.webtoon

import day.toons.domain.common.AuditingEntity
import java.time.DayOfWeek
import javax.persistence.*

@Table(name = "TB_WEBTOON")
@Entity
class Webtoon(
    name: String,
    thumbnail: String,
    dayOfWeek: DayOfWeek,
) : AuditingEntity() {
    @Column(nullable = false)
    var name = name
        protected set
    @Column(nullable = false)
    var thumnail = thumbnail
        protected set
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var dayOfWeek = dayOfWeek
        protected set
}