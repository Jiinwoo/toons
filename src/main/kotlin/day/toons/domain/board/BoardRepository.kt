package day.toons.domain.board

import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {
    fun findAllByTitle(title: String) : List<Board>
}
