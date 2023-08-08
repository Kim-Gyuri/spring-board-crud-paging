package test.lomboktest.web.dto;

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
    String type;


    @Builder
    public CreatePostRequest(String title, String content, String type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

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
