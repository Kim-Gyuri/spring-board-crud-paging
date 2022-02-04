package test.lomboktest.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.lomboktest.entities.Board;
import test.lomboktest.service.BoardService;
import org.springframework.data.domain.Pageable;

@Slf4j
@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/add")
    public String showAddForm(@RequestParam(value="id", defaultValue = "0") Long id, Model model) {
        model.addAttribute("board", new Board());
        return "/board/add-post";
    }

    @GetMapping("/board/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "/board/post";
    }

    @PostMapping("/addBoard")
    public String addBoard(@ModelAttribute("board") BoardForm board, BindingResult result){
        if (result.hasErrors()) {
            return "/board/add-post";
        }
        Long save = boardService.save(board);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showList(Pageable pageable, Model model) {
        Page<Board> boardList = boardService.findBoardList(pageable);
        boardList.stream().forEach(e -> e.getContent());
        model.addAttribute("boardList", boardList);
        return "/board/index";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "/board/update-post";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable("id") Long id, @Validated BoardForm board, BindingResult result) {
        if (result.hasErrors()) {
            board.builder().id(id);
            return "/board/update-post";
        }
        boardService.update(id, board);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        boardService.delete(id);
        return "redirect:/index";
    }


}