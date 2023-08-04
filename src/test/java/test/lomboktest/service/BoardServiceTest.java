package test.lomboktest.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import test.lomboktest.entities.dto.BoardForm;

import java.time.LocalDateTime;

@SpringBootTest
@Rollback(value = false)
class BoardServiceTest {

    @Autowired BoardService boardService;

    @Test
    public void 게시글수정() {

        BoardForm newBoard = BoardForm.builder()
                .title("newTitle")
                .content("enjoy")
                .updatedDate(LocalDateTime.now())
                .build();

        boardService.update(1L,newBoard);

        Assertions.assertThat(
                boardService.findById(1L).getContent()).isEqualTo("enjoy");

    }

}