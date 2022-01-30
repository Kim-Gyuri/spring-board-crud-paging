package test.lomboktest.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.controller.BoardForm;
import test.lomboktest.controller.UpdateForm;
import test.lomboktest.domain.Board;
import test.lomboktest.repository.BoardRepository;

@AllArgsConstructor
@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Long save(BoardForm boardForm) {
        return boardRepository.save(boardForm.toEntity()).getIdx();
    }

    @Transactional
    public void delete(Long idx) {
        Board board = boardRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + idx));
        boardRepository.delete(board);
    }

    @Transactional
    public Long update(Long idx, UpdateForm updateForm) {
        Board board = boardRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 업습니다. id= " + idx));
        board.update(updateForm.getTitle(), updateForm.getSubTitle(), updateForm.getContent());
        return idx;
    }
/*
    public void updatePost(Long idx, UpdateForm board) {

        Board b = findBoardByIdx(idx).builder()
                .title(board.getTitle())
                .subTitle(board.getSubTitle())
                .content(board.getContent())
                .boardType(board.getBoardType())
                .build();
        Board save = save(b);
        log.info("Board.getContent={}" , save.getContent());
    }
*/
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