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
    @NotNull
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private LocalDateTime updatedDate;

    public Board() {}

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
}
