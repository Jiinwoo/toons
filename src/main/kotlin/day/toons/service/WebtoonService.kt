package day.toons.service

import day.toons.domain.webtoon.Webtoon
import day.toons.domain.webtoon.WebtoonRepository
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek

@Transactional
@Service
class WebtoonService(
    private val webtoonRepository: WebtoonRepository
) {
    fun doCrawling() {
        webtoonRepository.deleteAll()
        val connect = Jsoup.connect("https://comic.naver.com/webtoon/weekday")
        val document = connect.get()
        val items = document.select(".list_area.daily_all .col .col_inner ul li")
        val webtoons = items.map { item ->
            val anchor = item.select(".thumb a")
            val link = anchor.attr("href")

            val imageTag = item.select(".thumb a img")
            val thumbNailImageUrl = imageTag.attr("src")
            val title = imageTag.attr("title")
            Webtoon(title, thumbNailImageUrl, DayOfWeek.FRIDAY)
        }
        webtoonRepository.saveAll(webtoons)

    }
}