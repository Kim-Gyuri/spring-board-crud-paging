package test.lomboktest.web.dto;

import lombok.Data;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.enums.BoardType;

import java.time.LocalDateTime;

@Data
public class CreatePostRequest {
    String title;
    String content;
    String type;


    public Board toEntity() {
        Board board = Board.boardBuilder()
                .title(title)
                .content(content)
                .boardType(BoardType.enumOf(type))
                .updatedDate(LocalDateTime.now())
                .build();
        return board;
    }
}
