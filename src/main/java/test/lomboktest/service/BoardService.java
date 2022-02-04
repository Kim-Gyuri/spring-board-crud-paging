package test.lomboktest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.controller.BoardForm;
import test.lomboktest.entities.Board;
import test.lomboktest.repository.BoardRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardForm boardForm) {

        return boardRepository.save(boardForm.toEntity()).getId();
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        boardRepository.delete(board);
    }

    @Transactional
    public Long update(Long id, BoardForm boardForm) {
        Board board = findById(id);
        board.update(boardForm.getTitle(), boardForm.getContent(), LocalDateTime.now());
        return id;
    }

    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber()-1,
                pageable.getPageSize());
        return boardRepository.findAll(pageable);
    }

    public Board findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board id= " + id ));
        return board;
    }

}