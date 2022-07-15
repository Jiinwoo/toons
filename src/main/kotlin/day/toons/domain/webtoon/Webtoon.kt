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
    platform: Platform,
    link: String
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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var platform = platform
        protected set
    @Column(nullable = false)
    var link = link
        protected set
}