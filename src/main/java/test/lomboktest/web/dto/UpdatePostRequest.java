package test.lomboktest.web.dto;

import lombok.Data;
@Data
public class UpdatePostRequest {
    private String title;
    private String content;
}
