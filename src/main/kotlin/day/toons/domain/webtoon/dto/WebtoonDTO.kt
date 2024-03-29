package day.toons.domain.webtoon.dto

import day.toons.domain.common.Platform
import java.time.DayOfWeek

class WebtoonDTO(
        val id: Long?,
        val name: String,
        val thumbnail: String,
        val dayOfWeek: DayOfWeek,
        val platform: Platform,
        val link: String,
)
