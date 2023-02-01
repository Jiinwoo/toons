package day.toons.service

import day.toons.TestConfiguration
import day.toons.domain.common.Platform
import day.toons.domain.webtoon.Webtoon
import day.toons.domain.webtoon.WebtoonRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import javax.persistence.EntityManager

@SpringBootTest(classes = [TestConfiguration::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
@ActiveProfiles(value = ["test"])
internal class WebtoonServiceTest(
    private val webtoonRepository: WebtoonRepository,
    private val entityManager: EntityManager
) {

    companion object {
        private val dayOfWeekNumbers = listOf("", "mon", "tue", "wed", "thu", "fri", "sat", "sun")
    }

    @Test
    fun 크롤링() {
        val savedWebtoons = webtoonRepository.saveAll(
            (1..10).map {
                Webtoon(
                    it.toString(), "", DayOfWeek.FRIDAY, Platform.NAVER, ""
                )
            }
        )
        entityManager.flush()
        val updatedWebtoons = (1..3).map{
            Webtoon(
                it.toString(), "", DayOfWeek.FRIDAY, Platform.NAVER, ""
            )
        }
        val remove = savedWebtoons.toSet().subtract(updatedWebtoons.toSet())
        Assertions.assertThat(remove).size().isEqualTo(7)



    }
}
