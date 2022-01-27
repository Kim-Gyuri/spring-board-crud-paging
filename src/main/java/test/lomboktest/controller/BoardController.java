package test.lomboktest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import test.lomboktest.domain.Board;
import test.lomboktest.service.BoardService;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/add")
    public String board(@RequestParam(value="idx", defaultValue = "0") Long idx,
                        Model model) {
        model.addAttribute("board", boardService.findBoardByIdx(idx));
        return "/board/createForm";
    }

    @PostMapping("/add")
    public String post(@ModelAttribute BoardForm form, RedirectAttributes redirectAttributes) throws IOException {
        Board board = Board.builder()
                .title(form.getTitle())
                .subTitle(form.getSubTitle())
                .content(form.getContent())
                .boardType(form.getBoardType())
                .createdDate(LocalDateTime.now())
                .build();
        boardService.save(board);

        redirectAttributes.addAttribute("postId", board.getIdx());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/board/{postId}";
    }

    @GetMapping("/{postId}")
    public String posts(@PathVariable Long id,Model model) {
        Board board = boardService.findBoardByIdx(id);
        model.addAttribute("board", board);
        return "board/post";

    }

    @GetMapping("/list")
    public String list(@PageableDefault Pageable pageable, Model model) {
        Page<Board> boardList = boardService.findBoardList(pageable);
        boardList.stream().forEach(e -> e.getContent());
        model.addAttribute("boardList", boardList);

        return "/board/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("postId") Long id, Model model) {
        Board board = boardService.findBoardByIdx(id);
        BoardForm form = new BoardForm();
        form.setIdx(board.getIdx());
        form.setSubTitle(board.getSubTitle());
        form.setContent(board.getContent());
        form.setBoardType(board.getBoardType());
        model.addAttribute("board", board);
        return "editForm";
    }

    @PostMapping("/{id}/edit")
    public String editUpdate(@PathVariable("postId") Long id, @ModelAttribute("form") BoardForm form) {
        boardService.updatePost(id, form.getTitle(), form.getSubTitle(),form.getContent(), form.getBoardType());
        return "redirect:/board";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Board board = boardService.findBoardByIdx(id);
        boardService.delete(board);
        return "redirect:/board";
    }


}