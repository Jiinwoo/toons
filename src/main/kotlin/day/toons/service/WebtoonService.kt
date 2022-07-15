package day.toons.service

import day.toons.domain.webtoon.Platform
import day.toons.domain.webtoon.Webtoon
import day.toons.domain.webtoon.WebtoonRepository
import day.toons.global.util.URLUtils
import org.jsoup.Jsoup
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
        webtoonRepository.deleteAll()
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
        webtoonRepository.saveAll(webtoons)
    }
}