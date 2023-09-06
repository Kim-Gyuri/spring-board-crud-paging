package test.lomboktest.entities.enums;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum BoardType {
    notice("공지사항", "NOTICE"),
    free("자유게시판", "FREE");

    private String value;
    private String type;

    BoardType(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public static BoardType enumOf(String type) {
        return Arrays.stream(BoardType.values())
                .filter(t -> t.getType().equals(type))
                .findAny().orElse(null);
    }

}
