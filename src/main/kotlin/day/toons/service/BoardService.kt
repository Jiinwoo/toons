package day.toons.service

import day.toons.domain.board.Board
import day.toons.domain.board.BoardRepository
import day.toons.domain.board.dto.BoardCreateDTO
import day.toons.domain.board.dto.BoardDTO
import day.toons.domain.webtoon.dto.event.WebtoonSavedEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.event.TransactionalEventListener

private val logger = KotlinLogging.logger { }

@Transactional
@Service
class BoardService(
        private val boardRepository: BoardRepository
) {
    fun createBoardByAPI(dto: BoardCreateDTO) {
        logger.info { "createBoardByAPI, dto = $dto" }
        createBoard(dto)
    }

    /**
     * API, 컨트롤러 레벨에서 직접 호출하지마세요.
     */
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    fun createBoardByCron(webtoonSavedEvent: WebtoonSavedEvent) {
        logger.info { "webtoonSavedEvent = $webtoonSavedEvent" }
        try {
            createBoard(BoardCreateDTO(
                    title = webtoonSavedEvent.title,
                    description = "${webtoonSavedEvent.title}의 새로운 게시판입니다.",
                    thumbnail = webtoonSavedEvent.thumbnail,
                    dayOfWeek = webtoonSavedEvent.dayOfWeek,
                    platform = webtoonSavedEvent.platform,
                    link = webtoonSavedEvent.link
            ))
        } catch (e: Exception) {
            logger.error { "createBoardByCron, e = $e" }
        }
    }

    fun getBoard(webtoonName: String): BoardDTO {
        val boardList = boardRepository.findAllByTitle(webtoonName)
        if (boardList.isEmpty()) {
            throw IllegalArgumentException("해당 웹툰이 존재하지 않습니다.")
        }
        // 방어 코드
        if (boardList.size > 1) {
            logger.error { "같은 이름의 웹툰이 두개 이상있습니다. 이름 = $webtoonName" }
            throw IllegalArgumentException("같은 이름의 웹툰이 두개 이상있습니다. 관리자에게 문의해주세요.")
        }
        val board = boardList.first()
        return BoardDTO(
                title = board.title,
                thumbnail = board.thumbnail,
                description = board.description,
                dayOfWeek = board.dayOfWeek,
                platform = board.platform,
                link = board.link
        )
    }


    private fun createBoard(dto: BoardCreateDTO) {
        // 방어코드
        boardRepository.findAllByTitle(dto.title).let {
            if (it.isNotEmpty()) {
                logger.error { "같은 이름의 웹툰이 이미 존재합니다. 이름 = ${dto.title}" }
                throw IllegalArgumentException("같은 이름의 웹툰이 이미 존재합니다.")
            }
        }

        boardRepository.save(
                Board(
                        title = dto.title,
                        thumbnail = dto.thumbnail,
                        description = dto.description,
                        dayOfWeek = dto.dayOfWeek,
                        platform = dto.platform,
                        link = dto.link
                )
        )
    }

}
