package test.lomboktest.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.enums.BoardType;
import test.lomboktest.controller.dto.getPostResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Slf4j
@SpringBootTest
@Transactional
public class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;

    @AfterEach
    public void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        String title = "title_제목";
        String content = "content_내용";

        boardRepository.save(Board.boardBuilder()
                .title(title)
                .content(content)
                .updatedDate(LocalDateTime.now())
                .build());

        // when
        List<Board> board_list = boardRepository.findAll();

        // then
        Board find = board_list.get(board_list.size() - 1);
        assertThat(find.getTitle()).isEqualTo(title);
        assertThat(find.getContent()).isEqualTo(content);
    }

    @Test
    public void 게시물_타입별정렬() {
        //given
        String type = "FREE";

        //when
        List<getPostResponse> dtos = boardRepository.sortByBoardType(type);

        //then
        assertThat(dtos.get(0).getBoardType()).isEqualTo(BoardType.enumOf(type));
    }


}
