package test.lomboktest.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import test.lomboktest.entities.Board;
import test.lomboktest.service.BoardService;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final BoardService boardService;



    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<Board> boardList = boardService.findBoardList(pageable);
        boardList.stream().forEach(e -> e.getContent());
        model.addAttribute("boardList", boardList);
        return "index";
    }

    @GetMapping("/posts/add")
    public String postSaveForm() {
        return "posts-save";
    }

    @GetMapping("/posts/edit/{id}")
    public String postUpdateForm(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "update-post";
    }

    @GetMapping("/posts/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "posts-detail";
    }

}
