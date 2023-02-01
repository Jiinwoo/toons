package day.toons.domain.webtoon.dto.event

import day.toons.domain.common.Platform
import java.time.DayOfWeek

data class WebtoonSavedEvent(
        val title: String,
        val thumbnail: String,
        val dayOfWeek: DayOfWeek,
        val platform: Platform,
        val link: String,
)
