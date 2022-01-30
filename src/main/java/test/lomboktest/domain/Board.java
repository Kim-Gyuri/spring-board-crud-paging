package test.lomboktest.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.lomboktest.domain.enums.BoardType;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
@Table(name="board")
public class Board extends TimeEntity{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idx;

    @Column
    private String title;

    @Column
    private String subTitle;

    @Column
    @NotNull
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Builder
    public Board(Long idx, String title, String subTitle, String content, BoardType boardType) {
        this.idx = idx;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
    }

    public void update(String title, String subTitle, String content) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
    }

}
