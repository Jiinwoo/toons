package day.toons.domain.webtoon

import org.springframework.data.jpa.repository.JpaRepository

interface WebtoonRepository: JpaRepository<Webtoon, Long> {
}