package test.lomboktest.controller;


import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/add")
    public String board(@RequestParam(value="idx", defaultValue = "0") Long idx,
                        Model model) {
        model.addAttribute("board", new Board());
        return "/board/createForm";
    }

    @PostMapping("/add")
    public String post(@ModelAttribute("board") BoardForm boardform, RedirectAttributes redirectAttributes) throws IOException {
        Long idx = boardService.save(boardform);

        redirectAttributes.addAttribute("idx", idx);
        redirectAttributes.addAttribute("status", true);

        return "redirect:/board/{idx}";
    }

    @GetMapping("/{idx}")
    public String posts(@PathVariable("idx") Long idx, Model model) {
        Board board = boardService.findBoardByIdx(idx);
        model.addAttribute("boardForm", board);
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
    public String editForm(@PathVariable("idx") Long idx, Model model) {
        Board boardForm = boardService.findBoardByIdx(idx);
        model.addAttribute("boardForm", boardForm);
        log.info("선택한 게시판 id={}", idx);
        return "board/editForm";
    }

    @PostMapping("/{idx}/edit")
    public String editUpdate(@PathVariable("idx") Long idx, @ModelAttribute UpdateForm updateForm, RedirectAttributes redirectAttributes) {
        Long update = boardService.update(idx, updateForm);
        Board updateBoard = boardService.findBoardByIdx(update);

        log.info("업데이트한 게시판 id={}", updateBoard.getIdx());
        log.info("수정한 내용={}", updateBoard.getContent());

        redirectAttributes.addAttribute("idx", updateBoard.getIdx());
        redirectAttributes.addAttribute("status", true);


        return "redirect:/board/{idx}";
    }

    @GetMapping("/{idx}/delete")
    public String delete(@PathVariable Long idx) {
        boardService.delete(idx);
        return "redirect:/board/list";
    }


}