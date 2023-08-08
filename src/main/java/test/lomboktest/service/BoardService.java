package test.lomboktest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.dto.BoardForm;
import test.lomboktest.repository.BoardRepository;
import test.lomboktest.web.dto.CreatePostRequest;
import test.lomboktest.web.dto.MainPostDto;
import test.lomboktest.web.dto.UpdatePostRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public Long save(CreatePostRequest request) {
        return boardRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        boardRepository.delete(board);
    }
    @Transactional
    public void delete() {
      boardRepository.deleteAll();
    }

    @Transactional
    public Long update(Long id, BoardForm boardForm) {
        Board board = findById(id);
        board.update(boardForm.getTitle(), boardForm.getContent(), LocalDateTime.now());
        return id;
    }

    @Transactional
    public Long update(Long id, UpdatePostRequest request) {
        Board board = findById(id);
        board.update(request.getTitle(), request.getContent(), LocalDateTime.now());
        return id;
    }

    // 메인페이지에서 (1~n) 페이징으로 리스트 보기
    @Transactional(readOnly = true)
    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber()-1,
                pageable.getPageSize());
        return boardRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public List<MainPostDto> findBoardList() {
        List<MainPostDto> collect = boardRepository.findAll().stream()
                .map(o -> new MainPostDto(o.getId(), o.getTitle(), o.getContent(), o.getBoardType()))
                .collect(Collectors.toList());
        return collect;
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board id= " + id ));
        return board;
    }

    @Transactional(readOnly = true)
    public List<MainPostDto> findByBoardType(String type) {
        return boardRepository.sortByBoardType(type);
    }
}