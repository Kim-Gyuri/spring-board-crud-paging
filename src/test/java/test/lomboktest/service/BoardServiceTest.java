package test.lomboktest.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import test.lomboktest.domain.Board;
import test.lomboktest.domain.enums.BoardType;

@SpringBootTest
@Rollback(value = false)
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Test
    public void 게시글수정() {
        Board board = Board.builder()
                .idx(1L)
                .title("getTitle")
                .subTitle("sub")
                .content("jojoy")
                .boardType(BoardType.free)
                .build();
        Board save = boardService.save(board);


        Board newBoard = Board.builder()
                .title("new")
                .subTitle("new")
                .content("enjoy")
                .boardType(BoardType.free)
                .build();

        boardService.updatePost(1L, newBoard);
        Assertions.assertThat(
                boardService.findBoardByIdx(1L).getContent()).isEqualTo(newBoard.getContent());

    }

}