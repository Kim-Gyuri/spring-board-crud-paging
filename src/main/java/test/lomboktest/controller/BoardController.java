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
        model.addAttribute("board", new BoardForm());
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

    @GetMapping("/{idx}")
    public String posts(@PathVariable("idx") Long id, Model model) {
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

    @GetMapping("/{idx}/edit")
    public String editForm(@PathVariable("idx") Long id, Model model) {
        Board post = boardService.findOne(id);

        BoardForm board = new BoardForm();
        board.setIdx(post.getIdx());
        board.setTitle(post.getTitle());
        board.setSubTitle(post.getSubTitle());
        board.setContent(post.getContent());
        board.setBoardType(post.getBoardType());
        model.addAttribute("board", board);
        return "board/editForm";
    }

    @PostMapping("/{idx}/edit")
    public String editUpdate(@PathVariable("idx") Long idx, @ModelAttribute("board") BoardForm form, RedirectAttributes redirectAttributes) throws IOException {
        boardService.updatePost(idx, form.getTitle(), form.getSubTitle(),form.getContent(), form.getBoardType());

        redirectAttributes.addAttribute("idx",form.getIdx());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/board/{idx}";
    }

    @DeleteMapping("/{postId}/delete")
    public String delete(Long id) {
        boardService.delete(id);
        return "redirect:/board";
    }


}