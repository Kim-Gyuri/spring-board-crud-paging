package test.lomboktest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.enums.BoardType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initData();
    }

    @RequiredArgsConstructor
    @Component
    @Transactional
    static class InitService {

        private final EntityManager em;

        public void initData() {
            IntStream.rangeClosed(1, 154).forEach(i -> {
                Board boardEntity = getBoard(i);

                em.persist(boardEntity);

            });
        }
        private Board getBoard(int i) {
            Board boardEntity =  Board.boardBuilder()
                    .title("title" + i)
                    .content("content" + i)
                    .boardType(BoardType.free)
                    .updatedDate(LocalDateTime.now())
                    .build();
            return boardEntity;
        }
    }


}
