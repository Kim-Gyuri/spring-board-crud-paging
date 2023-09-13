package test.lomboktest.controller.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.lomboktest.controller.dto.*;
import test.lomboktest.service.BoardService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class BoardsApiController {

    private final BoardService boardService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreatePostResponse save(@RequestPart(value = "createDto") CreatePostRequest requestDto) {
        log.info("dto postType={}", requestDto.getBoardType());
        Long id = boardService.save(requestDto);

        return new CreatePostResponse(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public UpdatePostResponse update(@PathVariable("id") Long id, @Validated @RequestPart("updateDto") UpdatePostRequest requestDto) {
        return boardService.update(id, requestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public DeletePostResponse delete(@PathVariable Long id) {
        boardService.delete(id);
        return new DeletePostResponse(Boolean.TRUE, "success!");
    }

    @GetMapping("/{id}")
    public getPostResponse findById(@PathVariable Long id) {
        return boardService.getDetail(id);
    }

    // 전체조회 API
    @GetMapping
    public Result findAll() {
        List<getPostResponse> collect = boardService.findBoardList();
        return new Result(collect, collect.size());
    }

    // 게시물 분류타입별 조회 API
    @GetMapping("/type")
    public List<getPostResponse> findByBoardType(@RequestParam String type) {
        return boardService.findByBoardType(type);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
        private int count;
    }
}
