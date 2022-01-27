package test.lomboktest.controller;

import lombok.Getter;
import lombok.Setter;
import test.lomboktest.domain.enums.BoardType;

@Getter @Setter
public class BoardForm {
    private Long idx;

    private String title;

    private String subTitle;

    private String content;

    private BoardType boardType;

}
