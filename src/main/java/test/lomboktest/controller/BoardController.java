package test.lomboktest.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.lomboktest.entities.Board;
import test.lomboktest.entities.dto.BoardForm;
import test.lomboktest.service.BoardService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{id}")
    public String showPostDetail(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "/board/post";
    }

    @GetMapping("/add")
    public String showAddForm(@ModelAttribute("board") BoardForm board) {
        return "/board/add-post";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute("board") BoardForm board, BindingResult result){
        if (result.hasErrors()) {
            return "/board/add-post";
        }
        // 성공 로직
        boardService.save(board);
        return "redirect:/board/home";
    }

    // 메인 페이지
    @GetMapping("/home")
    public String showList(Pageable pageable, Model model) {
        Page<Board> boardList = boardService.findBoardList(pageable);
        boardList.stream().forEach(e -> e.getContent());
        model.addAttribute("boardList", boardList);
        return "/board/index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "/board/update-post";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable("id") Long id, @Validated BoardForm board, BindingResult result) {
        if (result.hasErrors()) {
            board.builder().id(id);
            return "/board/update-post";
        }
        // 성공 로직
        boardService.update(id, board);
        return "redirect:/board/home";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        boardService.delete(id);
        return "redirect:/board/home";
    }


}