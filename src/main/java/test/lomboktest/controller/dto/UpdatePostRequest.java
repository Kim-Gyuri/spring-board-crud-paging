package test.lomboktest.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdatePostRequest {
    private String title;
    private String content;
    private LocalDateTime updateTime;

    @Builder
    public UpdatePostRequest(String title, String content) {
        this.title = title;
        this.content = content;
        this.updateTime = LocalDateTime.now();
    }
}
