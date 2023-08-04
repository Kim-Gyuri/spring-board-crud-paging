package test.lomboktest.web.dto;

import lombok.Data;

@Data
public class CreatePostResponse {
    private Long id;

    public CreatePostResponse(Long id) {
        this.id = id;
    }
}
