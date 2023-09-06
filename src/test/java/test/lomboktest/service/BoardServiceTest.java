package test.lomboktest.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import test.lomboktest.controller.dto.UpdatePostRequest;

@SpringBootTest
@Rollback(value = false)
class BoardServiceTest {

    @Autowired BoardService boardService;

    @Test
    public void 게시글수정() {

        UpdatePostRequest newBoard = UpdatePostRequest.builder()
                .title("newTitle")
                .content("enjoy")
                .build();

        boardService.update(1L,newBoard);

        Assertions.assertThat(
                boardService.findById(1L).getContent()).isEqualTo("enjoy");

    }

}