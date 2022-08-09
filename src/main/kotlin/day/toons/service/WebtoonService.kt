package day.toons.service

import day.toons.domain.webtoon.Platform
import day.toons.domain.webtoon.Webtoon
import day.toons.domain.webtoon.WebtoonRepository
import day.toons.domain.webtoon.dto.WebtoonDTO
import day.toons.global.util.URLUtils
import org.jsoup.Jsoup
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL
import java.time.DayOfWeek

@Transactional
@Service
class WebtoonService(
    private val webtoonRepository: WebtoonRepository
) {
    companion object {
        private val dayOfWeekNumbers = listOf("", "mon", "tue", "wed", "thu", "fri", "sat", "sun")
    }

    fun doCrawling() {
        val savedWebtoons = webtoonRepository.findAll()
        val connect = Jsoup.connect("https://comic.naver.com/webtoon/weekday")
        val document = connect.get()
        val items = document.select(".list_area.daily_all .col .col_inner ul li")
        val webtoons = items.map { item ->
            val anchor = item.select(".thumb a")
            val link = anchor.attr("href")

            val url = URL("https://comic.naver.com${link}")
            val queryParameters = URLUtils.splitQuery(url)
            val dayOfWeek = DayOfWeek.of(dayOfWeekNumbers.indexOf(queryParameters["weekday"]!!.first()))

            val imageTag = item.select(".thumb a img")
            val thumbNailImageUrl = imageTag.attr("src")
            val title = imageTag.attr("title")
            Webtoon(title, thumbNailImageUrl, dayOfWeek, Platform.NAVER, url.toString())
        }

        webtoonRepository.deleteAllByIdInBatch(savedWebtoons.toSet().subtract(webtoons.toSet()).map { it.id })
        webtoonRepository.saveAll(webtoons.toSet().subtract(savedWebtoons.toSet()))
    }

    fun getWebtoons(pageable: Pageable, dayOfWeek: DayOfWeek?, platform: Platform?): Page<WebtoonDTO> {
        val webtoons = if (dayOfWeek != null) {
            if (platform != null) {
                webtoonRepository.findAllByDayOfWeekAndPlatformAndDeletedAtIsNull(dayOfWeek, platform, Pageable.unpaged())
            } else {
                webtoonRepository.findAllByDayOfWeekAndDeletedAtIsNull(dayOfWeek, Pageable.unpaged())
            }
        } else {
            if (platform != null) {
                webtoonRepository.findAllByPlatformAndDeletedAtIsNull(platform, pageable)
            } else {
                webtoonRepository.findAll(pageable)
            }
        }
        return webtoons.map { _webtoon ->
            WebtoonDTO(
                name = _webtoon.name,
                thumbnail = _webtoon.thumbnail,
                dayOfWeek = _webtoon.dayOfWeek,
                platform = _webtoon.platform,
                link = _webtoon.link,
            )
        }

    }
}