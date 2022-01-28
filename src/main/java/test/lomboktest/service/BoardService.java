package test.lomboktest.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test.lomboktest.domain.Board;
import test.lomboktest.domain.enums.BoardType;
import test.lomboktest.repository.BoardRepository;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public Board findOne(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NullPointerException());
    }

    public void delete(Long id) {
        Board board = findOne(id);
        boardRepository.delete(board);
    }

    public void updatePost(Long idx, String title, String subTitle, String content, BoardType boardType) {

        Board builder = findOne(idx).builder()
                .title(title)
                .subTitle(subTitle)
                .content(content)
                .boardType(boardType)
                .build();
        log.info("Board.getContent={}" , builder.getContent());
    }

    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber()-1,
                pageable.getPageSize());
        return boardRepository.findAll(pageable);
    }

    public Board findBoardByIdx(Long idx) {
        return boardRepository.findById(idx).orElse(new Board());
    }

}