package test.lomboktest.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.lomboktest.entities.Board;
import test.lomboktest.service.BoardService;
import test.lomboktest.web.dto.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class BoardApiController {

    private final BoardService boardService;

    // 등록 API
    @PostMapping
    public CreatePostResponse save(@RequestBody @Validated CreatePostRequest board) {
        Long id = boardService.save(board);
        return new CreatePostResponse(id);
    }

    // 수정 API
    @PatchMapping("/{id}")
    public UpdatePostResponse update(@PathVariable("id") Long id, @RequestBody @Validated UpdatePostRequest request ) {
        Board board = boardService.findById(id);
        board.update(request.getTitle(), request.getContent(), LocalDateTime.now());
        return new UpdatePostResponse(board.getId(), board.getTitle());
    }


    // 단건조회 API
    @GetMapping("/{id}")
    public MainPostDto findById(@PathVariable Long id) {
        Board board = boardService.findById(id);
        MainPostDto dto = new MainPostDto(board);
        return dto;
    }

    // 전체조회 API
    @GetMapping
    public Result findAll() {
        List<MainPostDto> collect = boardService.findBoardList();
        return new Result(collect, collect.size());
    }

    // 게시물 분류타입별 조회 API
    @GetMapping("/category")
    public List<MainPostDto> findByBoardType(@RequestParam String type) {
        return boardService.findByBoardType(type);
    }

    // 삭제 API
    @DeleteMapping("/{id}")
    public DeletePostResponse delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return new DeletePostResponse(Boolean.TRUE, "success!");
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
        private int count;
    }
}
