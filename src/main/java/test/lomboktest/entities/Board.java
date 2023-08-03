package test.lomboktest.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.lomboktest.entities.enums.BoardType;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@Table(name="board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @Column(name ="BOARD_ID")
    @GeneratedValue
    public Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private LocalDateTime updatedDate;

    @Builder(builderMethodName = "boardBuilder")
    public Board(String title, String content, BoardType boardType, LocalDateTime updatedDate) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.updatedDate = updatedDate;
    }

    public void update(String title, String content, LocalDateTime updatedDate) {
        this.title = title;
        this.content = content;
        this.updatedDate = updatedDate;
    }

    public Board toEntity() {
        return Board.boardBuilder()
                .title(title)
                .content(content)
                .boardType(boardType)
                .updatedDate(updatedDate)
                .build();
    }

    @Override
    public String toString() {
        return "Board{" + "id=" + id + ", title=" + title + ", content=" + content + ", boardType=" + boardType + '}';
    }
}
