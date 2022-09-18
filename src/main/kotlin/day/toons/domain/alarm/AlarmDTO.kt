package day.toons.domain.alarm

import day.toons.domain.webtoon.Platform
import java.time.DayOfWeek
import java.time.LocalDateTime

class AlarmDTO(
    val id: Long,
    val webtoonDTO: AlarmWebtoonDTO
)

class AlarmWebtoonDTO(
    val name: String,
    val thumbnail: String,
    val dayOfWeek: DayOfWeek,
    val platform: Platform,
    val link: String,
    val deletedAt: LocalDateTime?
)