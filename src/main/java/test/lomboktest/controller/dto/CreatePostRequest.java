package test.lomboktest.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.enums.BoardType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreatePostRequest {
    String title;
    String content;
    String boardType;

    @Builder
    public CreatePostRequest(String title, String content, String boardType) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
    }

    public Board toEntity() {
        Board board = Board.boardBuilder()
                .title(title)
                .content(content)
                .boardType(BoardType.enumOf(boardType))
                .updatedDate(LocalDateTime.now())
                .build();
        return board;
    }
}
