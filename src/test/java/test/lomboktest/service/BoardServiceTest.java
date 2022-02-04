package test.lomboktest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class BoardServiceTest {

    @Autowired
    BoardService boardService;
/*
    @Autowired
    Board board;

    @Test
    public void 게시글수정() {
        BoardForm Board = BoardForm.builder()
                .id(1L)
                .title("getTitle")
                .content("jojo")
                .boardType(BoardType.free)
                .updatedDate(LocalDateTime.now())
                .build();
        Long saveId = boardService.save(Board);

        BoardForm newBoard = BoardForm.builder()
                .id(1L)
                .title("newTitle")
                .content("enjoy")
                .boardType(BoardType.free)
                .updatedDate(LocalDateTime.now())
                .build();

        boardService.update(saveId,newBoard);

        Assertions.assertThat(
                boardService.findBoardById(1L).getContent()).isEqualTo("enjoy");

    }
*/
}