package day.toons.infra.crawler

import day.toons.domain.webtoon.NAVERCrawler
import day.toons.domain.webtoon.Platform
import day.toons.domain.webtoon.Webtoon
import day.toons.global.util.URLUtils
import day.toons.service.WebtoonService
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek

@Component
class JsoupNaverCrawlerClient: NAVERCrawler {

    companion object {
        private val dayOfWeekNumbers = listOf("", "mon", "tue", "wed", "thu", "fri", "sat", "sun")
    }

    override fun getWebtoonList(): Result<List<Webtoon>> = runCatching {
        val doc = Jsoup.connect("https://comic.naver.com/webtoon/weekday.nhn").get()
        val webtoonList = doc.select("div.col_inner ul.img_list li")
        webtoonList.map { item ->
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
    }

    override fun getCompletedWebtoonTitles(): Result<List<String>> = runCatching {
        val connect = Jsoup.connect("https://comic.naver.com/webtoon/finish?order=Update&view=image")
        val document = connect.get()
        val items = document.select(".list_area li")
        items.map { item ->
            val anchor = item.select(".thumb a")
            anchor.attr("title")
        }.take(30)
    }
}