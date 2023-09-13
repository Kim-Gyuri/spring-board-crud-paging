package test.lomboktest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import test.lomboktest.entities.Board;

@Data
@AllArgsConstructor
public class UpdatePostResponse {
    private Long id;
    private String title;

    public UpdatePostResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
    }
}
