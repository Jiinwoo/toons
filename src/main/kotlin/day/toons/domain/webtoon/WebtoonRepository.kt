package day.toons.domain.webtoon

import day.toons.domain.common.Platform
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.DayOfWeek

interface WebtoonRepository: JpaRepository<Webtoon, Long> {
    fun findAllByDayOfWeekAndDeletedAtIsNull(dayOfWeek: DayOfWeek, pageable: Pageable): Page<Webtoon>
    fun findAllByDayOfWeekAndPlatformAndDeletedAtIsNull(dayOfWeek: DayOfWeek, platform: Platform, pageable: Pageable): Page<Webtoon>
    fun findAllByPlatformAndDeletedAtIsNull(platform: Platform, pageable: Pageable): Page<Webtoon>
    fun findTop30ByDeletedAtIsNotNullAndPlatformOrderByDeletedAtDesc(platform: Platform): List<Webtoon>
    fun findAllByDeletedAtIsNullAndPlatform(platform: Platform): List<Webtoon>
}
