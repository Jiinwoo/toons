package day.toons.infra.crawler

import day.toons.domain.webtoon.KAKAOCrawler
import day.toons.domain.common.Platform
import day.toons.domain.webtoon.Webtoon
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.DayOfWeek

// 월 화 수 목 금 토 일 을 dayOfWeek 로 변환
fun String.toDayOfWeek(): DayOfWeek {
    return when (this) {
        "월" -> DayOfWeek.MONDAY
        "화" -> DayOfWeek.TUESDAY
        "수" -> DayOfWeek.WEDNESDAY
        "목" -> DayOfWeek.THURSDAY
        "금" -> DayOfWeek.FRIDAY
        "토" -> DayOfWeek.SATURDAY
        "일" -> DayOfWeek.SUNDAY
        else -> {
            throw IllegalArgumentException("요일이 잘못되었습니다.")
        }
    }
}

@Component
class WebClientKAKAOClient(
    private val webClient: WebClient
) : KAKAOCrawler {
    override fun getWebtoonList(): Result<List<Webtoon>> = runCatching {
        val generalWebtoons = webClient.get()
            .uri("section/v2/pages/general-weekdays")
            .retrieve()
            .bodyToMono(KAKAOWebtoonDTO::class.java)
            .block()!!.data.sections.flatMap { section ->
                section.cardGroups.flatMap { cardGroup ->
                    cardGroup.cards.map { card ->
                        Webtoon(
                            name = card.content.title,
                            dayOfWeek = section.title.toDayOfWeek(),
                            thumbnail = card.content.featuredCharacterImageB,
                            platform = Platform.KAKAO,
                            link = "${card.content.title}/${card.content.id}",
                        )
                    }
                }
            }
        val novelWebtoons = webClient.get()
            .uri("section/v2/pages/novel-weekdays")
            .retrieve()
            .bodyToMono(KAKAOWebtoonDTO::class.java)
            .block()!!.data.sections.flatMap { section ->
                section.cardGroups.flatMap { cardGroup ->
                    cardGroup.cards.map { card ->
                        Webtoon(
                            name = card.content.title,
                            dayOfWeek = section.title.toDayOfWeek(),
                            thumbnail = card.content.featuredCharacterImageB,
                            platform = Platform.KAKAO,
                            link = "https://webtoon.kakao.com/content/${card.content.title}/${card.content.id}",
                        )
                    }
                }
            }
        novelWebtoons + generalWebtoons
    }

    override fun getWebtoonTitles(): Result<List<String>> = kotlin.runCatching {
        webClient.get()
            .uri("section/v2/sections?placement=channel_completed")
            .retrieve()
            .bodyToMono(KAKAOCompleteWebtoonDTO::class.java)
            .block()!!.data.flatMap { section ->
                section.cardGroups.flatMap { cardGroup ->
                    cardGroup.cards.map { card ->
                        card.content.title
                    }
                }
            }.take(30)

    }
}

data class KAKAOWebtoonDTO(
    val data: Data
) {
    data class Data constructor(
        val sections: List<Section>
    ) {
        data class Section private constructor(
            val cardGroups: List<CardGroup>,
            val title: String
        ) {
            data class CardGroup private constructor(
                val cards: List<Card>
            ) {
                data class Card private constructor(
                    val content: Content
                ) {
                    data class Content private constructor(
                        val title: String,
                        val featuredCharacterImageB: String,
                        val id: Long
                    )
                }
            }
        }
    }
}

data class KAKAOCompleteWebtoonDTO(
    val data: List<Data>
) {
    data class Data constructor(
        val cardGroups: List<KAKAOWebtoonDTO.Data.Section.CardGroup>
    )
}
