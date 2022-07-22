package day.toons.domain.webtoon

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek

@RestController
@RequestMapping("/api/webtoons")
class WebtoonApi(
    private val webtoonRepository: WebtoonRepository
) {
    @GetMapping
    fun getWebtoons(
        pageable: Pageable,
        @RequestParam(name = "day-of-week") dayOfWeek: DayOfWeek? = null,
        @RequestParam(name = "platform") platform: Platform? = null
    ): Page<Webtoon> {
        if (dayOfWeek != null) {
            if (platform != null) {
                return webtoonRepository.findAllByDayOfWeekAndPlatform(dayOfWeek, platform, Pageable.unpaged())
            }else {
                return webtoonRepository.findAllByDayOfWeek(dayOfWeek, Pageable.unpaged())
            }
        }else {
            if (platform != null) {
                return webtoonRepository.findAllByPlatform(platform, pageable)
            }else {
                return webtoonRepository.findAll(pageable)
            }
        }
    }

}