package day.toons.domain.webtoon

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/webtoons")
class WebtoonApi(
    private val webtoonRepository: WebtoonRepository
) {

    @GetMapping
    fun getWebtoons(
        pageable: Pageable
    ): Page<Webtoon> {
        return webtoonRepository.findAll(pageable)
    }

}