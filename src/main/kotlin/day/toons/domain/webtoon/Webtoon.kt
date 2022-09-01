package day.toons.domain.webtoon

import day.toons.domain.common.AuditingEntity
import mu.KotlinLogging
import org.hibernate.annotations.SQLDelete
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

private val logger = KotlinLogging.logger {}
@Table(name = "TB_WEBTOON")
@Entity
@SQLDelete(sql = "UPDATE tb_webtoon SET deleted_at = NOW() WHERE id = ?")
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
    @Column(nullable = true)
    var deletedAt: LocalDateTime? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Webtoon

        if (name != other.name) return false
        if (thumbnail != other.thumbnail) return false
        if (dayOfWeek != other.dayOfWeek) return false
        if (platform != other.platform) return false
        if (link != other.link) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + thumbnail.hashCode()
        result = 31 * result + dayOfWeek.hashCode()
        result = 31 * result + platform.hashCode()
        result = 31 * result + link.hashCode()
        return result
    }

    fun delete(): Webtoon{
        this.deletedAt = LocalDateTime.now()
        return this
    }

    fun changeDayOfWeek(dayOfWeek: DayOfWeek): Webtoon{
        this.dayOfWeek = dayOfWeek
        return this
    }

    fun changeThumbnail(thumbnail: String): Webtoon {
        this.thumbnail = thumbnail
        return this
    }


}