package test.lomboktest.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.enums.BoardType;
@Data
public class MainPostDto {
    private Long id;
    private String title;
    private String content;
    private BoardType boardType;
    @QueryProjection
    public MainPostDto(Long id, String title, String content, BoardType boardType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
    }

    public MainPostDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
    }
}
