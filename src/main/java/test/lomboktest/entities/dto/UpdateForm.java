package test.lomboktest.entities.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateForm {

    private String title;
    private String subTitle;
    private String content;

    @Builder
    public UpdateForm(String title, String subTitle, String content) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
    }
}
