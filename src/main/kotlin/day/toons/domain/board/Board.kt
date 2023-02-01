package day.toons.domain.board

import day.toons.domain.common.AuditingEntity
import day.toons.domain.common.Platform
import java.time.DayOfWeek
import javax.persistence.*


@Entity
@Table(name = "TB_BOARD")
class Board(
        title: String,
        description: String,
        thumbnail: String,
        dayOfWeek: DayOfWeek,
        platform: Platform,
        link: String
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set
    @Column(name = "description", nullable = false)
    var description: String = description
        protected set
    @Column(nullable = false)
    var thumbnail = thumbnail
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
