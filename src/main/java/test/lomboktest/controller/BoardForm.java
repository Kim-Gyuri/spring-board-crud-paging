package test.lomboktest.controller;

import lombok.Data;
import test.lomboktest.domain.enums.BoardType;

@Data
public class BoardForm {
    private Long idx;

    private String title;

    private String subTitle;

    private String content;

    private BoardType boardType;

}
