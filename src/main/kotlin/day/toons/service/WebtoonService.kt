package day.toons.service

import day.toons.domain.alarm.AlarmRepository
import day.toons.domain.alarm.AlarmSms
import day.toons.domain.alarm.AlarmStatus
import day.toons.domain.common.Platform
import day.toons.domain.webtoon.*
import day.toons.domain.webtoon.dto.WebtoonDTO
import day.toons.domain.webtoon.dto.event.WebtoonSavedEvent
import day.toons.global.error.exception.BusinessException
import day.toons.global.error.exception.ErrorCode
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek

private val logger = KotlinLogging.logger {}

@Transactional
@Service
class WebtoonService(
        private val webtoonRepository: WebtoonRepository,
        private val alarmRepository: AlarmRepository,
        private val alarmSms: AlarmSms,
        private val naverCrawler: NAVERCrawler,
        private val kakaoCrawler: KAKAOCrawler,
        private val eventPublisher: ApplicationEventPublisher
) {


    fun crawerNAVER() {
        val savedWebtoons = webtoonRepository.findAllByDeletedAtIsNullAndPlatform(Platform.NAVER)
        val webtoons = naverCrawler.getWebtoonList()
                .getOrElse { ex ->
                    logger.error(ex) { "네이버 웹툰 크롤링 실패" }
                    throw BusinessException(ErrorCode.CRAWLER_FAIL)
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
        val news = webtoons.filterNot { new ->
            (savedWebtoons + updatedWebtoons).any { it.name == new.name && it.dayOfWeek == new.dayOfWeek }
        }
        news.forEach { newWebtoon ->
            eventPublisher.publishEvent(
                    WebtoonSavedEvent(
                            title = newWebtoon.name,
                            thumbnail = newWebtoon.thumbnail,
                            dayOfWeek = newWebtoon.dayOfWeek,
                            platform = newWebtoon.platform,
                            link = newWebtoon.link
                    )
            )
        }
        webtoonRepository.saveAll(updatedWebtoons + news)
    }

    fun crawlingNAVERComplete() {
        val titles = naverCrawler.getCompletedWebtoonTitles()
                .getOrElse { ex ->
                    logger.error(ex) { "네이버 완결 웹툰 크롤링 실패" }
                    throw BusinessException(ErrorCode.CRAWLER_FAIL)
                }
        val deletedWebtoons =
                webtoonRepository.findTop30ByDeletedAtIsNotNullAndPlatformOrderByDeletedAtDesc(Platform.NAVER)
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

    fun crawerKAKAO() {
        val savedWebtoons = webtoonRepository.findAllByDeletedAtIsNullAndPlatform(Platform.KAKAO)
        val webtoons = kakaoCrawler.getWebtoonList()
                .getOrElse { ex ->
                    logger.error(ex) { "카카오 웹툰 크롤링 실패" }
                    throw BusinessException(ErrorCode.CRAWLER_FAIL)
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
        val news = webtoons.filterNot { new ->
            (savedWebtoons + updatedWebtoons).any { it.name == new.name && it.dayOfWeek == new.dayOfWeek }
        }
        webtoonRepository.saveAll(updatedWebtoons + news)
    }

    fun crawlingKAKAOComplete() {
        val titles = kakaoCrawler.getWebtoonTitles().getOrElse { ex ->
            logger.error(ex) { "카카오 완결 웹툰 크롤링 실패" }
            throw BusinessException(ErrorCode.CRAWLER_FAIL)
        }
        val deletedWebtoons =
                webtoonRepository.findTop30ByDeletedAtIsNotNullAndPlatformOrderByDeletedAtDesc(Platform.KAKAO)
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
}
