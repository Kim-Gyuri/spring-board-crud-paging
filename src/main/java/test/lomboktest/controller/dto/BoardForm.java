package test.lomboktest.controller.dto;

import lombok.*;
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

    @Builder
    public BoardForm(Long id, String title, String content, BoardType boardType, LocalDateTime updatedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.updatedDate = updatedDate;
    }
}
