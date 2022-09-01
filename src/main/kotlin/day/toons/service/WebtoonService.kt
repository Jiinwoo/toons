package day.toons.service

import day.toons.domain.alarm.AlarmRepository
import day.toons.domain.alarm.AlarmSms
import day.toons.domain.alarm.AlarmStatus
import day.toons.domain.webtoon.Platform
import day.toons.domain.webtoon.Webtoon
import day.toons.domain.webtoon.WebtoonRepository
import day.toons.domain.webtoon.dto.WebtoonDTO
import day.toons.global.util.URLUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.zip
import java.net.URL
import java.time.DayOfWeek

private val logger = KotlinLogging.logger {}

@Transactional
@Service
class WebtoonService(
    private val webtoonRepository: WebtoonRepository,
    private val alarmRepository: AlarmRepository,
    private val alarmSms: AlarmSms
) {
    companion object {
        private val dayOfWeekNumbers = listOf("", "mon", "tue", "wed", "thu", "fri", "sat", "sun")
    }

    fun crawlingSerialize() {
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

        val updatedWebtoons = savedWebtoons
            .filterNot { savedWebtoon ->
                // 완전히 동일한 엔티티가 있을 경우 제외
                webtoons.any { it == savedWebtoon }
            }
            .map { savedWebtoon ->
                val matched = webtoons.find { it.dayOfWeek == savedWebtoon.dayOfWeek && it.name == savedWebtoon.name }
                if (matched != null) {
                    savedWebtoon.changeThumbnail(matched.thumbnail)
                } else {
                    val existOtherDayOfWeek =
                        webtoons.find { it.dayOfWeek != savedWebtoon.dayOfWeek && it.name == savedWebtoon.name }
                    if (existOtherDayOfWeek != null) {
                        savedWebtoon.changeDayOfWeek(existOtherDayOfWeek.dayOfWeek)
                    } else {
                        savedWebtoon.delete()
                    }
                }
            }
        val news = webtoons.filterNot {new ->
            (savedWebtoons + updatedWebtoons).any { it.name == new.name && it.dayOfWeek == new.dayOfWeek }
        }

        webtoonRepository.saveAll(updatedWebtoons + news)


    }

    fun crawlingComplete() {
        val connect = Jsoup.connect("https://comic.naver.com/webtoon/finish?order=Update&view=image")
        val document = connect.get()
        val items = document.select(".list_area li")

        val titles = items.map { item ->
            val anchor = item.select(".thumb a")
            anchor.attr("title")
        }.take(30)
        val deletedWebtoons = webtoonRepository.findTop30ByDeletedAtIsNotNullOrderByDeletedAtDesc()
        val completedWebtoons = deletedWebtoons.filter { deletedWebtoon ->
            titles.any { it == deletedWebtoon.name }
        }
        val alarms = alarmRepository.findAllByWebtoonInAndStatusNot(completedWebtoons, AlarmStatus.SUCCESS)

        val completedList = runBlocking {
            alarms.map {
                Pair(async { alarmSms.sendOut(it.member.phoneNumber!!, it.webtoon.name) }, it)
            }.map { (api, alarm) ->
                api
                    .await()
                    .onSuccess {
                        alarm.complete()
                    }
                    .onFailure {
                        logger.error { it }
                        alarm.fail()
                    }
                alarm
            }
        }
        alarmRepository.saveAll(completedList)


    }

    fun getWebtoons(pageable: Pageable, dayOfWeek: DayOfWeek?, platform: Platform?): Page<WebtoonDTO> {
        val webtoons = if (dayOfWeek != null) {
            if (platform != null) {
                webtoonRepository.findAllByDayOfWeekAndPlatformAndDeletedAtIsNull(
                    dayOfWeek,
                    platform,
                    Pageable.unpaged()
                )
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
                id = _webtoon.id,
                name = _webtoon.name,
                thumbnail = _webtoon.thumbnail,
                dayOfWeek = _webtoon.dayOfWeek,
                platform = _webtoon.platform,
                link = _webtoon.link,
            )
        }

    }
}