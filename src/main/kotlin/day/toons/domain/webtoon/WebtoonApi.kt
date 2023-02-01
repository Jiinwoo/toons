package day.toons.domain.webtoon

import day.toons.domain.common.Platform
import day.toons.domain.webtoon.dto.WebtoonDTO
import day.toons.service.WebtoonService
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
    private val webtoonService: WebtoonService
) {
    @GetMapping
    fun getWebtoons(
        pageable: Pageable,
        @RequestParam(name = "day-of-week") dayOfWeek: DayOfWeek? = null,
        @RequestParam(name = "platform") platform: Platform? = null
    ): Page<WebtoonDTO> {
        return webtoonService.getWebtoons(pageable, dayOfWeek, platform)
    }

}
