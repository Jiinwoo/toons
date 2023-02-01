package day.toons.domain.board.dto

import day.toons.domain.common.Platform
import java.time.DayOfWeek


class BoardDTO(
        val title: String,
        val thumbnail: String,
        val dayOfWeek: DayOfWeek,
        val platform: Platform,
        val link: String,
        val description: String
)

class BoardCreateDTO(
        val title: String,
        val thumbnail: String,
        val dayOfWeek: DayOfWeek,
        val platform: Platform,
        val link: String,
        val description: String
)

class BoardUpdateDTO {

}



