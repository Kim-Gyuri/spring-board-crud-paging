package test.lomboktest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.entities.Board;
import test.lomboktest.service.BoardService;
import test.lomboktest.web.dto.CreatePostRequest;
import test.lomboktest.web.dto.MainPostDto;
import test.lomboktest.web.dto.UpdatePostRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class BoardApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MockMvc mvc;

    @AfterEach
    public void cleanUp() {
        boardService.delete();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 등록하기() throws Exception {
        //given
        CreatePostRequest dto = getCreatePostRequest();

        String url = "http://localhost:" + port + "/api/post";

        //when
        mvc.perform(post(url)
                .content(new ObjectMapper().writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        List<MainPostDto> all = boardService.findBoardList();
        MainPostDto new_board = all.get(all.size() - 1);
        assertThat(new_board.getTitle()).isEqualTo(dto.getTitle());
        assertThat(new_board.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 수정하기() throws Exception {
        //given
        CreatePostRequest create_dto = getCreatePostRequest();
        Long id = boardService.save(create_dto);

        UpdatePostRequest update_dto = getUpdatePostRequest();

        String url = "http://localhost:" + port + "/api/post/" + id;

        //when
        mvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(new ObjectMapper().writeValueAsString(update_dto)))
                .andExpect(status().isOk());

        // then
        Board update_board = boardService.findById(id);
        assertThat(update_board.getTitle()).isEqualTo(update_dto.getTitle());
        assertThat(update_board.getContent()).isEqualTo(update_dto.getContent());
    }

    private static UpdatePostRequest getUpdatePostRequest() {
        String expected_title = "title_제목2";
        String expected_content = "content_내용2";

        UpdatePostRequest dto = UpdatePostRequest.builder()
                .title(expected_title)
                .content(expected_content)
                .build();
        return dto;
    }

    private static CreatePostRequest getCreatePostRequest() {
        String title = "title_제목";
        String content = "content_내용";
        String type = "FREE";

        CreatePostRequest dto = CreatePostRequest.builder()
                .title(title)
                .content(content)
                .type(type)
                .build();
        return dto;
    }

}