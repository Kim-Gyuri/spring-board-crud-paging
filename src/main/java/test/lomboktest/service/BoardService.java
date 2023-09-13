package test.lomboktest.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.lomboktest.controller.dto.UpdatePostResponse;
import test.lomboktest.entities.Board;
import test.lomboktest.repository.BoardRepository;
import test.lomboktest.controller.dto.CreatePostRequest;
import test.lomboktest.controller.dto.UpdatePostRequest;
import test.lomboktest.controller.dto.getPostResponse;
import test.lomboktest.utils.exception.post.NotFoundPostException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(CreatePostRequest request) {
        return boardRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostException("해당 번호 "+ id+"는 존재하지 않습니다."));
        boardRepository.delete(board);
    }

    @Transactional
    public void delete() {
      boardRepository.deleteAll();
    }

    @Transactional
    public UpdatePostResponse update(Long id, UpdatePostRequest request) {
        Board board = findById(id);
        log.info("update before={}", board.toString());
        board.update(request.getTitle(), request.getContent(), LocalDateTime.now());
        return new UpdatePostResponse(board);
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
    public List<getPostResponse> findBoardList() {
        List<getPostResponse> collect = boardRepository.findAll().stream()
                .map(o -> new getPostResponse(o.getId(), o.getTitle(), o.getContent(), o.getBoardType()))
                .collect(Collectors.toList());
        return collect;
    }

    @Transactional(readOnly = true)
    public getPostResponse getDetail(Long id) {
        Board board = findById(id);
        return new getPostResponse(board);
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundPostException("해당 번호는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<getPostResponse> findByBoardType(String type) {
        return boardRepository.sortByBoardType(type);
    }

}