package day.toons.domain.board

import day.toons.domain.board.dto.BoardCreateDTO
import day.toons.domain.board.dto.BoardDTO
import day.toons.service.BoardService
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder

@RestController
@RequestMapping("/api/boards")
class BoardAPI(
        private val boardService: BoardService
) {

    /**
     * 게시판 목록 조회
     * ROLE_ANONYMOUS
     */
    @GetMapping
    fun getBoards() {

    }

    /**
     * 게시판 상세 조회
     * ROLE_ANONYMOUS
     */
    @GetMapping("/{encodedWebtoonName}")
    fun getBoard(@PathVariable encodedWebtoonName: String): BoardDTO{
        return boardService.getBoard(URLDecoder.decode(encodedWebtoonName, "UTF-8"))
    }


    /**
     * 게시판 생성
     * ROLE_ADMIN
     */
    @PostMapping
    fun createBoard(
            @RequestBody dto: BoardCreateDTO
    ) {
        boardService.createBoardByAPI(dto)
    }

//    /**
//     * 게시판 수정을 위한 조회
//     * ROLE_ADMIN or ROLE_STAFF_{board_name}
//     */
//    @GetMapping("/{boardId}")
//    fun getBoard(@PathVariable boardId: String) {
//
//    }
//
//    /**
//     * 게시판 수정
//     * ROLE_ADMIN or ROLE_STAFF_{board_name}
//     */
//    @PutMapping("/{boardId}")
//    fun updateBoard(@PathVariable boardId: String) {
//
//    }

    /**
     * 게시판 삭제
     * soft delete
     * ROLE_ADMIN
     */
    @DeleteMapping("/{boardId}")
    fun deleteBoard(@PathVariable boardId: String) {

    }
}
