package day.toons.domain.alarm

import day.toons.domain.webtoon.Platform
import day.toons.domain.webtoon.dto.WebtoonDTO
import java.time.DayOfWeek

class AlarmDTO(
    val webtoonDTO: AlarmWebtoonDTO
)

class AlarmWebtoonDTO(
    val name: String,
    val thumbnail: String,
    val dayOfWeek: DayOfWeek,
    val platform: Platform,
    val link: String,
)