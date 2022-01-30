package test.lomboktest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import test.lomboktest.domain.Board;
import test.lomboktest.domain.User;
import test.lomboktest.domain.enums.BoardType;
import test.lomboktest.repository.BoardRepository;
import test.lomboktest.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = userRepository.save(User.builder()
                .name("saelobi")
                .password("saelobi")
                .email("saelobi@gmail.com")
                .createdDate(LocalDateTime.now())
                .build());

        IntStream.rangeClosed(1, 200).forEach(index ->
                boardRepository.save(Board.builder()
                        .title("Content " + index)
                        .subTitle("Order " + index)
                        .content("Content Example " + index)
                        .boardType(BoardType.free)
//                        .createdDate(LocalDateTime.now())
  //                      .updatedDate(LocalDateTime.now())
    //                    .user(user).build()));
                .build()));
    }

}
