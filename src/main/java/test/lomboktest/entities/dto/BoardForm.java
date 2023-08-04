package test.lomboktest.entities.dto;

import lombok.Builder;
import lombok.Data;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.enums.BoardType;

import java.time.LocalDateTime;

@Data
public class BoardForm {
    private Long id;
    private String title;
    private String content;
    private BoardType boardType;
    private LocalDateTime updatedDate;

    public Board toEntity() {
        Board board = Board.boardBuilder()
                .title(title)
                .content(content)
                .boardType(boardType)
                .updatedDate(LocalDateTime.now())
                .build();
        return board;
    }


    // test 할 때 사용하려고 만들었다.
    @Builder
    public BoardForm(Long id, String title, String content, BoardType boardType, LocalDateTime updatedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.updatedDate = updatedDate;
    }

}
