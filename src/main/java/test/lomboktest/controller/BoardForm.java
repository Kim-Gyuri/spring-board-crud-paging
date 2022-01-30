package test.lomboktest.controller;

import lombok.*;
import test.lomboktest.domain.Board;
import test.lomboktest.domain.enums.BoardType;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardForm {
    private Long idx;

    private String title;

    private String subTitle;

    private String content;

    private BoardType boardType;

    public Board toEntity() {
        Board board = Board.builder()
                .title(title)
                .subTitle(subTitle)
                .content(content)
                .boardType(boardType)
                .build();
        return board;
    }

    @Builder
    public BoardForm(Long idx, String title, String subTitle, String content, BoardType boardType) {
        this.idx = idx;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
    }
}
