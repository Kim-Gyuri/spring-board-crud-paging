package test.lomboktest.utils;


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
        initService.initData1();
        initService.initData2();

    }

    @RequiredArgsConstructor
    @Component
    @Transactional
    static class InitService {

        private final EntityManager em;

        public void initData1() {
            IntStream.rangeClosed(0, 154).forEach(i -> {

                Board boardEntity = Board.boardBuilder()
                        .title("title" + i)
                        .content("content" + i)
                        .boardType(BoardType.free)
                        .updatedDate(LocalDateTime.now())
                        .build();

                em.persist(boardEntity);
            });
        }
        public void initData2() {
            IntStream.rangeClosed(0, 50).forEach(i -> {

                Board boardEntity = Board.boardBuilder()
                        .title("other title" + i)
                        .content("content" + i)
                        .boardType(BoardType.free)
                        .updatedDate(LocalDateTime.now())
                        .build();

                em.persist(boardEntity);
            });
        }


    }


}
