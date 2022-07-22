package day.toons.domain.webtoon

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.DayOfWeek

interface WebtoonRepository: JpaRepository<Webtoon, Long> {
    fun findAllByDayOfWeek(dayOfWeek: DayOfWeek, pageable: Pageable): Page<Webtoon>
    fun findAllByDayOfWeekAndPlatform(dayOfWeek: DayOfWeek, platform: Platform, pageable: Pageable): Page<Webtoon>
    fun findAllByPlatform(platform: Platform, pageable: Pageable): Page<Webtoon>


}